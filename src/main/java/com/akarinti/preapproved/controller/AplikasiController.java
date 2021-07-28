package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponseDTO;
import com.akarinti.preapproved.service.AplikasiService;
import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/aplikasi")
public class AplikasiController {

    @Autowired
    AplikasiService aplikasiService;

//    @PreAuthorize("hasAuthority('ACCESS_RUMAHSAYA_SERVICES')")
    @PostMapping(value = "/rumahsaya",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> createData(@Valid @RequestBody RumahSayaDTO rumahSayaDTO) {
        RumahSayaResponseDTO response = aplikasiService.createData(rumahSayaDTO);
        return ResponseEntity.ok(new ResultDTO(StatusCode.OK.message(), response));
    }

    @GetMapping(value = "/rumahsaya",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> getData(@RequestParam(required = false) String search,
                                             @RequestParam(required = false) String status,
                                             @PageableDefault(size = 20) Pageable pageable) {
        ResultDTO response = aplikasiService.getData(search, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/image/download/{secureIdAplikasi}/{fileName}")
    public ResponseEntity<ResultDTO> getImage(HttpServletResponse response,
                             @PathVariable("secureIdAplikasi") String secureIdAplikasi,
                             @PathVariable("fileName") String fileName
    ) throws IOException {
        Map<String, Object> map = aplikasiService.downloadImage(secureIdAplikasi, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResultDTO resultDTO = new ResultDTO(StatusCode.OK.message(), map);
        ResponseEntity<ResultDTO> responseEntity = new ResponseEntity<>(resultDTO, headers, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/file/download/{secureIdAplikasi}/{fileName}")
    public void downloadFile(HttpServletResponse response,
                             @PathVariable("secureIdAplikasi") String secureIdAplikasi,
                             @PathVariable("fileName") String fileName
    ) throws IOException {
        Map<String, Object> map = aplikasiService.downloadImage(secureIdAplikasi, fileName);
        File file = (File) map.get("file");

        InputStream inputStream = new FileInputStream(file);
        response.setHeader("Content-Disposition", "filename=" + file.getName());
        IOUtils.copy(inputStream, response.getOutputStream());

        response.flushBuffer();
        inputStream.close();
    }

}
