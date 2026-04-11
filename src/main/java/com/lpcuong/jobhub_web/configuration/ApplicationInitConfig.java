package com.lpcuong.jobhub_web.configuration;

import com.lpcuong.jobhub_web.entity.UserEntity;
import com.lpcuong.jobhub_web.enums.Role;
import com.lpcuong.jobhub_web.enums.Status;
import com.lpcuong.jobhub_web.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByemail("admin@gmail.com") == null) {
                String role = Role.ADMIN.name();
                UserEntity userEntity = UserEntity.builder()
                        .email("admin@gmail.com")
                        .role(role)
                        .password(passwordEncoder.encode("admin123"))
                        .status(Status.ACTIVE.name())
                        .build();
                userRepository.save(userEntity);
                log.warn("admin user has been created with default password: admin123, please change it");
            }
        };
    }
}
