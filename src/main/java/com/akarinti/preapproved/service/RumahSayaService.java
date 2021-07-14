package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.rumahsaya.RumahSayaRequestDTO;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.entity.RumahSaya;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import com.akarinti.preapproved.jpa.repository.RumahSayaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class RumahSayaService {

    @Autowired
    RumahSayaRepository rumahSayaRepository;

    @Autowired
    AplikasiRepository aplikasiRepository;

    // TODO: add validation
    @Transactional
    public boolean createData(RumahSayaRequestDTO rumahSayaRequestDTO) {
        RumahSaya rumahSaya = RumahSaya.fromDTO(rumahSayaRequestDTO);
        rumahSayaRepository.save(rumahSaya);

        Aplikasi aplikasi = new Aplikasi();
        aplikasi.setRumahSaya(rumahSaya);
        aplikasiRepository.save(aplikasi);
        return true;
    }

}
