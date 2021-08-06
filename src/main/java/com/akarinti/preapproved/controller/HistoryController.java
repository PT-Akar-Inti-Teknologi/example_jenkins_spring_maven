package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @GetMapping(value = "/general-overview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> generalOverview() {
        ResultDTO response = historyService.generalOverview();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/completed-overview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> completedOverview() {
        ResultDTO response = historyService.completedOverview();
        return ResponseEntity.ok(response);
    }

}
