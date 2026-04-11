package com.lpcuong.jobhub_web.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    INVALID_KEY(99999, "Invalid Key"),
    USER_EXISTED(1001, "User existed"),
    USER_DOES_NOT_EXIST(1002, "User does not exist"),
    INVALID_PASSWORD(1003, "Password must be at least 6 characters"),
    INVALID_EMAIL(1004, "Email is not in the correct format"),
    INVALID_ROLE(1006, "Role is invalid"),
    AUTHENTICATED(1005, "Authenticated");
    int code;
    String message;
}
