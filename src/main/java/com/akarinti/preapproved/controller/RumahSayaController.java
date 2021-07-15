package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.authentication.SignInRequestDTO;
import com.akarinti.preapproved.dto.authentication.SignInResponseDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaRequestDTO;
import com.akarinti.preapproved.service.RumahSayaService;
import com.akarinti.preapproved.service.SignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/rumahsaya")
public class RumahSayaController {

    @Autowired
    RumahSayaService rumahSayaService;

    @PreAuthorize("hasAuthority('ACCESS_RUMAHSAYA_SERVICES')")
    @PostMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> loginUser(@RequestBody RumahSayaRequestDTO rumahSayaRequestDTO) {
        Boolean response = rumahSayaService.createData(rumahSayaRequestDTO);
//        if(response == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
        return ResponseEntity.ok(new ResultDTO(HttpStatus.OK.name(), response));
    }

}
