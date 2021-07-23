package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.MetaPaginationDTO;
import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponseDTO;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.predicate.AplikasiPredicate;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AplikasiService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AplikasiRepository aplikasiRepository;

    @Autowired
    HelperService helperService;

    @Transactional
    public RumahSayaResponseDTO createData(RumahSayaDTO rumahSayaDTO) {
        Aplikasi aplikasi = Aplikasi.fromDTO(rumahSayaDTO);
        aplikasiRepository.save(aplikasi);

        return new RumahSayaResponseDTO();
    }

    @SneakyThrows
    @Transactional
    public ResultDTO getData(String search, Pageable pageable) {
        Page<Aplikasi> aplikasiList;

        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("created_time", "creationDate");
        columnMapping.put("nama_lengkap", "namaLengkap");
        columnMapping.put("service_level", "creationDate");
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = "created_time";
        for (Sort.Order order: pageable.getSort()) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection();
        }
        Sort sort = Sort.by(sortDirection, columnMapping.get(sortProperty));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if(search != null) {
            aplikasiList = aplikasiRepository.findAll(AplikasiPredicate.aplikasiWithSearch(search), pageable);
        }  else {
            aplikasiList = aplikasiRepository.findAll(pageable);
        }

        List<RumahSayaDTO> rumahSayaDTOList = new ArrayList<>();
        for (Aplikasi aplikasi: aplikasiList.getContent()) {
            Date createdDate = Date.from(aplikasi.getCreationDate().atZone(ZoneId.of("GMT+7")).toInstant());
//
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
//            String dateInString = "2021-07-21 17:00";
//            Date date = formatter.parse(dateInString);
//            log.info("current date: "+ date);

            long remainingSL = helperService.getServiceLevelRemaining(createdDate);
            RumahSayaDTO rumahSayaDTO = RumahSayaDTO.fromEntity(aplikasi);
            rumahSayaDTO.setSisaSL(remainingSL);
            rumahSayaDTOList.add(rumahSayaDTO);
        }
        return new ResultDTO(new MetaPaginationDTO(aplikasiList), rumahSayaDTOList);

    }

}
