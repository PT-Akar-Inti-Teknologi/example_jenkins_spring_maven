package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.PlatformGenerator;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformGeneratorRepository extends BaseRepository<PlatformGenerator> {
    long countByFlagIgnoreCase(String status);
    long countByFlagIn(List<String> status);
}
