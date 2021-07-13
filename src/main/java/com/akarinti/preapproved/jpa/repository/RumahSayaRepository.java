package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.RumahSaya;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RumahSayaRepository extends JpaRepository<RumahSaya, Integer> {

}
