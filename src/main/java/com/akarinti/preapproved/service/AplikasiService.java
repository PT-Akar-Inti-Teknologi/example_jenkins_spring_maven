package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.ResultPageDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponseDTO;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.predicate.AplikasiPredicate;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AplikasiService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AplikasiRepository aplikasiRepository;

    @Transactional
    public RumahSayaResponseDTO createData(RumahSayaDTO rumahSayaDTO) {
        Aplikasi aplikasi = Aplikasi.fromDTO(rumahSayaDTO);
        aplikasiRepository.save(aplikasi);

        return new RumahSayaResponseDTO();
    }

    @Transactional
    public ResultPageDTO getData(String search, Pageable pageable) {
        Page<Aplikasi> aplikasiList = aplikasiRepository.findAll(AplikasiPredicate.aplikasiWithSearch(search), pageable);
        List<RumahSayaDTO> rumahSayaDTOList = new ArrayList<>();
        for (Aplikasi aplikasi: aplikasiList.getContent()) {
            rumahSayaDTOList.add(RumahSayaDTO.fromEntity(aplikasi));
        }
        return new ResultPageDTO(aplikasiList, rumahSayaDTOList);

    }

}
