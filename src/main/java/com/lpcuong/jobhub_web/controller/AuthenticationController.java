package com.lpcuong.jobhub_web.controller;

import com.lpcuong.jobhub_web.dto.reponse.ApiResponse;
import com.lpcuong.jobhub_web.dto.reponse.AuthenticationResponse;
import com.lpcuong.jobhub_web.dto.reponse.IntrospectResponse;
import com.lpcuong.jobhub_web.dto.request.AuthenticationRequest;
import com.lpcuong.jobhub_web.dto.request.IntrospectRequest;
import com.lpcuong.jobhub_web.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        IntrospectResponse introspectResponse = authenticationService.introspect(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(introspectResponse)
                .build();
    }

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) throws ParseException {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResponse)
                .build();
    }
}
