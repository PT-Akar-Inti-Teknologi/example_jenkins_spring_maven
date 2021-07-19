package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.ResultPageDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponseDTO;
import com.akarinti.preapproved.service.AplikasiService;
import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<ResultDTO> getData(@RequestParam(required = false) String search, @PageableDefault(size = 20) Pageable pageable) {
        ResultPageDTO response = aplikasiService.getData(search, pageable);
        return ResponseEntity.ok(response);
    }

}
