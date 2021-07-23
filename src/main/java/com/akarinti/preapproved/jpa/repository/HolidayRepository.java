package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.Holiday;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HolidayRepository extends BaseRepository<Holiday> {
    Holiday findByTanggal(Date tanggal);

    List<Holiday> findAllByTanggalBetween(Date startDate, Date endDate);
}
