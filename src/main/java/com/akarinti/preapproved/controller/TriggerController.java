package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.DataResponseDTO;
import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.service.AplikasiService;
import com.akarinti.preapproved.service.DataManagementService;
import com.akarinti.preapproved.utils.exception.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trigger")
public class TriggerController {

    @Autowired
    DataManagementService dataManagementService;

    @Autowired
    AplikasiService aplikasiService;

    @GetMapping(value = "/sync-holiday",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResultDTO> syncHoliday(){
        Boolean response = dataManagementService.syncHoliday();
        return ResponseEntity.ok(new ResultDTO(StatusCode.OK, response));
    }

    @PostMapping(value = "/retry-error-cbas",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> retryError() {
        DataResponseDTO response = aplikasiService.retryErrorCBAS();
        return ResponseEntity.ok(new ResultDTO(StatusCode.OK, response));
    }


}
