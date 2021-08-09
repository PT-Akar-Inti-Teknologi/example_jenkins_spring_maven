package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.Aplikasi;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AplikasiRepository extends BaseRepository<Aplikasi>, QuerydslPredicateExecutor<Aplikasi> {
    long countByStatusIgnoreCase(String status);
    long countByStatusIn(List<String> status);
    List<Aplikasi> findAllByStatusIgnoreCase(String status);
}
