package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.MetaPaginationDTO;
import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.activityLog.ActivityLogResponseDTO;
import com.akarinti.preapproved.jpa.entity.ActivityLog;
import com.akarinti.preapproved.jpa.predicate.ActivityLogPredicate;
import com.akarinti.preapproved.jpa.repository.ActivityLogRepository;
import com.akarinti.preapproved.utils.ExcelUtil;
import com.querydsl.core.BooleanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@Transactional
public class ActivityLogService {

    @Autowired
    ActivityLogRepository activityLogRepository;

    @Autowired
    ExcelUtil excelUtil;

    @SneakyThrows
    @Transactional
    public ResultDTO getList(String search, String status, String date, Pageable pageable) {
        Page<ActivityLog> activityLogs;

        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("created_time", "creationDate");
        columnMapping.put("nama_lengkap", "aplikasi.namaLengkap");
        columnMapping.put("email", "aplikasi.email");
        columnMapping.put("user_id_bca", "userBCA.fullName");
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = "created_time";
        for (Sort.Order order: pageable.getSort()) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection();
        }
        Sort sort = Sort.by(sortDirection, columnMapping.get(sortProperty));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        if(search != null || status != null || date != null) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(ActivityLogPredicate.activityLogWithSearch(status, date));
            activityLogs = activityLogRepository.findAll(builder, pageable);
        }  else {
            activityLogs = activityLogRepository.findAll(pageable);
        }

        List<ActivityLogResponseDTO> activityLogResponseDTOList = new ArrayList<>();
        for (ActivityLog activityLog: activityLogs.getContent()) {
            ActivityLogResponseDTO activityLogResponseDTO = ActivityLogResponseDTO.fromEntity(activityLog);
            activityLogResponseDTOList.add(activityLogResponseDTO);
        }
        return new ResultDTO(new MetaPaginationDTO(activityLogs), activityLogResponseDTOList);
    }

    public Map<String, Object> download(String search, String status, String date, Pageable pageable) {
        List<ActivityLog> activityLogs;

        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("created_time", "creationDate");
        columnMapping.put("nama_lengkap", "aplikasi.namaLengkap");
        columnMapping.put("email", "aplikasi.email");
        columnMapping.put("user_id_bca", "userBCA.fullName");
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = "created_time";
        for (Sort.Order order: pageable.getSort()) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection();
        }
        Sort sort = Sort.by(sortDirection, columnMapping.get(sortProperty));

        if(search != null || status != null || date != null) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(ActivityLogPredicate.activityLogWithSearch(status, date));
            activityLogs = IterableUtils.toList(activityLogRepository.findAll(builder, sort));
        }  else {
            activityLogs = activityLogRepository.findAll(sort);
        }
        log.info("activityLogs: "+ activityLogs);

        List<Object[]> rowData = new ArrayList<>();
        Locale id = new Locale("in", "ID");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm", id);
        for (ActivityLog activityLog: activityLogs) {
            Object[] row = new Object[5];

            final String formattedDate = activityLog.getCreationDate()
                    .atZone(ZoneId.of("GMT+7"))
                    .format(formatter);
            row[0] = formattedDate;
            row[1] = activityLog.getAplikasi().getNamaLengkap();
            row[2] = activityLog.getAplikasi().getEmail();
            row[3] = activityLog.getUserBCA().getUserIdPic();
            row[4] = activityLog.getStatus();

            rowData.add(row);
        }

        Date now = new Date();
        formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        Instant instant = now.toInstant();
        ZoneId zoneId = ZoneId.of("GMT+7");
        LocalDateTime ldt = instant.atZone(zoneId).toLocalDateTime();
        String formattedDate = ldt.format(formatter);
        String fileName = "ACTIVITY_LOG_"+ formattedDate+ ".xlsx";

        List<String[]> listHeader = new ArrayList<>();
        listHeader.add(new String[] {
                "Tanggal Waktu", // 0
                "Nama Nasabah", // 1
                "Email Nasabah", // 2
                "User ID Verifier", // 3
                "Action", // 4
        });

        List<String> listSheetName = new ArrayList<>();
        listSheetName.add("Data");

        List<List<Object[]>> listData = new ArrayList<>();
        listData.add(rowData);
        File file;
        try {
            file = excelUtil.generateExcel(listSheetName, listHeader, listData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashedMap<>();
        resultMap.put("file", file);
        resultMap.put("fileName", fileName);
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        resultMap.put("contentType", contentType);
        return resultMap;
    }
}
