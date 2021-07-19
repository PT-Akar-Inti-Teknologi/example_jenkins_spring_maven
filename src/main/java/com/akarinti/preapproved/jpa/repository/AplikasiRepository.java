package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.Aplikasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AplikasiRepository extends JpaRepository<Aplikasi, Integer>, QuerydslPredicateExecutor<Aplikasi> {

}
