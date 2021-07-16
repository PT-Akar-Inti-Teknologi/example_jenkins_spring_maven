package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaRequestDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponsetDTO;
import com.akarinti.preapproved.service.AplikasiService;
import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/aplikasi")
public class AplikasiController {

    @Autowired
    AplikasiService aplikasiService;

//    @PreAuthorize("hasAuthority('ACCESS_RUMAHSAYA_SERVICES')")
    @PostMapping(value = "/rumahsaya",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> createData(@RequestBody RumahSayaRequestDTO rumahSayaRequestDTO) {
        RumahSayaResponsetDTO response = aplikasiService.createData(rumahSayaRequestDTO);
        return ResponseEntity.ok(new ResultDTO(StatusCode.OK.message(), response));
    }

}
