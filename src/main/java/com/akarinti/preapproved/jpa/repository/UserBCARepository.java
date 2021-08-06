package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.UserBCA;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBCARepository extends JpaRepository<UserBCA, Integer> {
    UserBCA findByEmail(String email);
}
