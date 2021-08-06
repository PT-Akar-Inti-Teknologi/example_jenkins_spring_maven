package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.DataResponseDTO;
import com.akarinti.preapproved.dto.MetaPaginationDTO;
import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import com.akarinti.preapproved.dto.nlo.RequestCBASDTO;
import com.akarinti.preapproved.dto.nlo.RequestCBASPayloadDTO;
import com.akarinti.preapproved.dto.rumahsaya.ApplicationDataRequestDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaCreateResponseDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.entity.PlatformGenerator;
import com.akarinti.preapproved.jpa.entity.SLIK;
import com.akarinti.preapproved.jpa.entity.UserBCA;
import com.akarinti.preapproved.jpa.predicate.AplikasiPredicate;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import com.akarinti.preapproved.jpa.repository.PlatformGeneratorRepository;
import com.akarinti.preapproved.jpa.repository.SLIKRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@Transactional
public class HistoryService {

    @Autowired
    AplikasiRepository aplikasiRepository;

    @Autowired
    PlatformGeneratorRepository platformGeneratorRepository;

    public ResultDTO generalOverview() {
        long countOnProgress = aplikasiRepository.countByStatusIgnoreCase("PENDING_KAFKA");
        List<String> statusList = Arrays.asList("DELIVERED_RS", "REJECTED");
        long countCompleted = aplikasiRepository.countByStatusIn(statusList);
        long countError = aplikasiRepository.countByStatusIgnoreCase("ERROR_KAFKA");

        HashMap<String, Long> overview = new HashMap<>();
        overview.put("on_progress", countOnProgress);
        overview.put("completed", countCompleted);
        overview.put("error", countError);
        return new ResultDTO(overview);
    }

    public ResultDTO completedOverview() {
        List<String> statusList = Arrays.asList("DELIVERED_RS", "REJECTED");
        long countCompleted = aplikasiRepository.countByStatusIn(statusList);
        long countEligible = platformGeneratorRepository.countByFlagIgnoreCase("ELIGIBLE");
        long countNonEligible1 = aplikasiRepository.countByStatusIgnoreCase("NON_ELIGIBLE_1");
        long countNonEligible2 = aplikasiRepository.countByStatusIgnoreCase("NON_ELIGIBLE_2");
        long countNonEligible3 = aplikasiRepository.countByStatusIgnoreCase("NON_ELIGIBLE_3");
        long countRejected = aplikasiRepository.countByStatusIgnoreCase("REJECTED");

        HashMap<String, Long> overview = new HashMap<>();
        overview.put("total", countCompleted);
        overview.put("eligible", countEligible);
        overview.put("non_eligible_1", countNonEligible1);
        overview.put("non_eligible_2", countNonEligible2);
        overview.put("non_eligible_3", countNonEligible3);
        overview.put("rejected", countRejected);
        return new ResultDTO(overview);
    }

}
