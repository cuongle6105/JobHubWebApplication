package com.lpcuong.jobhub_web.service;


import ch.qos.logback.core.spi.ErrorCodes;
import com.lpcuong.jobhub_web.dto.reponse.UserResponse;
import com.lpcuong.jobhub_web.dto.request.UserCreationRequest;
import com.lpcuong.jobhub_web.dto.request.UserUpdateRequest;
import com.lpcuong.jobhub_web.entity.UserEntity;
import com.lpcuong.jobhub_web.enums.Role;
import com.lpcuong.jobhub_web.enums.Status;
import com.lpcuong.jobhub_web.exception.AppException;
import com.lpcuong.jobhub_web.exception.ErrorCode;
import com.lpcuong.jobhub_web.mapper.UserMapper;
import com.lpcuong.jobhub_web.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByemail(userCreationRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserEntity userEntity = userMapper.toUserEntity(userCreationRequest);
        userEntity.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        try{
            userEntity.setRole(String.valueOf(Role.valueOf(userCreationRequest.getRole().toUpperCase())));
        } catch (IllegalArgumentException e){
            throw new AppException(ErrorCode.INVALID_ROLE);
        }
        userEntity.setStatus(Status.ACTIVE.name());
        userRepository.save(userEntity);
        return userMapper.toUserResponse(userEntity);
    }
    public List<UserResponse> getUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(String userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));
        return userMapper.toUserResponse(userEntity);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));
        userMapper.updateUser(userEntity, userUpdateRequest);

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
