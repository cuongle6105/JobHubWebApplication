package com.lpcuong.jobhub_web.dto.reponse;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    @Email
    String email;
    String password;
    String status;
    String created_at;
    String updated_at;
}
