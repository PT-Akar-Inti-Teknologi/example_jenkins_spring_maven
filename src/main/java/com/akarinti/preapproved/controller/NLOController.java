package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.service.NLOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/nlo")
public class NLOController {

    @Autowired
    NLOService nloService;

    @PostMapping(value = "/slik-response",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> submitResponse(@RequestHeader("app-access-key") String appAccessKey, @RequestBody Map<String, Object> body) {
        HashMap<String, String> response = nloService.submitResponse(appAccessKey, body);
        return ResponseEntity.ok(new ResultDTO(response));
    }

}
