package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.service.ActivityLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/activity-log")
public class ActivityLogController {

    @Autowired
    ActivityLogService activityLogService;

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> getData(@RequestParam(required = false) String search,
                                             @RequestParam(required = false) String status,
                                             @RequestParam(required = false) String date,
                                             @PageableDefault(size = 20) Pageable pageable) {
        ResultDTO response = activityLogService.getList(search, status, date, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/download",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void downloadSurveyReport(
            HttpServletResponse response,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date,
            @PageableDefault(size = 20) Pageable pageable) throws IOException {

        Map<String, Object> mapFile = activityLogService.download(search, status, date, pageable);
        File file = (File) mapFile.get("file");
        String contentType = (String) mapFile.get("contentType");

        log.info("fileName: "+ mapFile.get("fileName"));
        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                .filename((String) mapFile.get("fileName"))
                .build();

        InputStream inputStream = new FileInputStream(file);
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        IOUtils.copy(inputStream, response.getOutputStream());

        response.flushBuffer();
        file.deleteOnExit();
        inputStream.close();
    }

}
