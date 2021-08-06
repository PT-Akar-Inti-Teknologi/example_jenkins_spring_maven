package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.entity.SLIK;
import org.springframework.stereotype.Repository;

@Repository
public interface SLIKRepository extends BaseRepository<SLIK> {
    SLIK findTopByRequestId(String requestId);
    SLIK findTopByAplikasi(Aplikasi aplikasi);

}
