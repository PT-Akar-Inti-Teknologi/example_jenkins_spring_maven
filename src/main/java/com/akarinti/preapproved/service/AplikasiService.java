package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.rumahsaya.RumahSayaRequestDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponsetDTO;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AplikasiService {

    @Autowired
    AplikasiRepository aplikasiRepository;

    // TODO: add validation
    @Transactional
    public RumahSayaResponsetDTO createData(RumahSayaRequestDTO rumahSayaRequestDTO) {
        Aplikasi aplikasi = Aplikasi.fromDTO(rumahSayaRequestDTO);
        aplikasiRepository.save(aplikasi);

        RumahSayaResponsetDTO rumahSayaResponsetDTO = new RumahSayaResponsetDTO();
        rumahSayaResponsetDTO.setSuccess(true);
        return rumahSayaResponsetDTO;
    }

}
