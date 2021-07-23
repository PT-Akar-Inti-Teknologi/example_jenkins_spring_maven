package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
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

    @GetMapping(value = "/sync-holiday",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResultDTO> syncHoliday(){
        Boolean response = dataManagementService.syncHoliday();
        return ResponseEntity.ok(new ResultDTO(StatusCode.OK.message(), response));
    }


}
