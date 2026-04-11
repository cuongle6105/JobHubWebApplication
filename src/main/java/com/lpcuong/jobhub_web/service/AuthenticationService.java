package com.lpcuong.jobhub_web.service;

import com.lpcuong.jobhub_web.dto.reponse.AuthenticationResponse;
import com.lpcuong.jobhub_web.dto.reponse.IntrospectResponse;
import com.lpcuong.jobhub_web.dto.request.AuthenticationRequest;
import com.lpcuong.jobhub_web.dto.request.IntrospectRequest;
import com.lpcuong.jobhub_web.entity.UserEntity;
import com.lpcuong.jobhub_web.exception.AppException;
import com.lpcuong.jobhub_web.exception.ErrorCode;
import com.lpcuong.jobhub_web.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    private String generateToken(UserEntity userEntity) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .issuer("lpcuong.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userId", "Custom")
                .build();
        Payload payload = new Payload(jwtClaimSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserEntity UserEntity = userRepository.findByemail(authenticationRequest.getEmail());
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), UserEntity.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.AUTHENTICATED);
        String token = generateToken(UserEntity);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

}
