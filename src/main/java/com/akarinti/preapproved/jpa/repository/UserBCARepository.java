package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.UserBCA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBCARepository extends JpaRepository<UserBCA, Integer> {

}
