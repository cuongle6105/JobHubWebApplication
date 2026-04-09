package com.lpcuong.jobhub_web.mapper;

import com.lpcuong.jobhub_web.dto.reponse.UserResponse;
import com.lpcuong.jobhub_web.dto.request.UserCreationRequest;
import com.lpcuong.jobhub_web.dto.request.UserUpdateRequest;
import com.lpcuong.jobhub_web.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(UserEntity userEntity);
    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);
}
