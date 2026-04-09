package com.lpcuong.jobhub_web.repository;

import com.lpcuong.jobhub_web.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByemail(String email);
    Boolean existsByemail(String email);
}
