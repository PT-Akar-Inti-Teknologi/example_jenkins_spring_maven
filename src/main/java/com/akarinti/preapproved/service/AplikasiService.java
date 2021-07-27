package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.MetaPaginationDTO;
import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponseDTO;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.predicate.AplikasiPredicate;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import com.akarinti.preapproved.utils.FileUtil;
import com.akarinti.preapproved.utils.HelperUtil;
import com.akarinti.preapproved.utils.exception.CustomException;
import com.akarinti.preapproved.utils.exception.StatusCode;
import com.querydsl.core.BooleanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AplikasiService {

    @Value("${hcp.url}")
    String hcpUrl;

    @Autowired
    AplikasiRepository aplikasiRepository;

    @Autowired
    HelperUtil helperService;

    @Autowired
    FileUtil fileUtil;

    @Transactional
    public RumahSayaResponseDTO createData(RumahSayaDTO rumahSayaDTO) {
        Aplikasi aplikasi = Aplikasi.fromDTO(rumahSayaDTO);
        aplikasiRepository.save(aplikasi);

        return new RumahSayaResponseDTO();
    }

    @SneakyThrows
    @Transactional
    public ResultDTO getData(String search, String status, Pageable pageable) {
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

        if(search != null || status != null) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(AplikasiPredicate.aplikasiWithStatus(status)).and(AplikasiPredicate.aplikasiWithSearch(search));
            aplikasiList = aplikasiRepository.findAll(builder, pageable);
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

    @SneakyThrows
    public Map<String, Object> downloadImage(String secureIdAplikasi, String fileName) {
        Aplikasi aplikasi = aplikasiRepository.findBySecureId(secureIdAplikasi);
        if (aplikasi == null) throw new CustomException(StatusCode.NOT_FOUND, new StatusCodeMessageDTO("data aplikasi tidak ditemukan", "application data not found"));

        Map<String, Object> resultMap = new HashMap<>();
        byte[] image;
        try {
            StringBuilder fileUrl = new StringBuilder();
            fileUrl.append(hcpUrl);
            fileUrl.append("/");
            fileUrl.append(aplikasi.getAppDataID());
            fileUrl.append("/");
            fileUrl.append(fileName);
            InputStream inputStream = fileUtil.getFileHCP(fileUrl.toString());
            // TODO: determine content type from file name
            String contentType = "application/octet-stream";
            resultMap.put("contentType", contentType);

            image = IOUtils.toByteArray(inputStream);
            resultMap.put("byte", image);

        } catch (Exception ex) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
            //Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);a
        }
        return resultMap;
    }

}
