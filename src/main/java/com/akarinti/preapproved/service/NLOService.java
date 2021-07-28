package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.nlo.RequestCBASPayloadDTO;
import com.akarinti.preapproved.dto.nlo.RequestCBASResponseDTO;
import com.akarinti.preapproved.dto.apiresponse.BCAErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class NLOService {
    private static String appAccessKey;
    @Value("${nlo.appAccessKey}")
    public void setAppAccessKey(String appAccessKey) {
        NLOService.appAccessKey = appAccessKey;
    }

    private static String appSource;
    @Value("${nlo.appSource}")
    public void setAppSource(String appSource) {
        NLOService.appSource = appSource;
    }

    private static String cbasUrl;
    @Value("${nlo.cbas.url}")
    public void setCbasUrl(String cbasUrl) {
        NLOService.cbasUrl = cbasUrl;
    }

    @SneakyThrows
    public static RequestCBASResponseDTO requestCBAS(RequestCBASPayloadDTO requestCBASPayloadDTO) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestCBASPayloadDTO);
        } catch (Exception e) {
            log.error("ERROR");
        }
        HttpResponse<JsonNode> response = null;
        try {
//            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .post(cbasUrl)
                    .header("Content-Type", "application/json")
                    .header("app-access-key", appAccessKey)
                    .header("app-source", appSource)
                    .body(jsonInString)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + cbasUrl);
        }
        String apiResponse = response.getBody().toString();
        if (response.getStatus() != 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            BCAErrorResponse bcaErrorResponse = objectMapper.readValue(apiResponse, BCAErrorResponse.class);
            String errorMessage = (String) bcaErrorResponse.getError_message().get("indonesian");
            throw new RuntimeException(errorMessage);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        RequestCBASResponseDTO requestCBASResponseDTO = objectMapper.readValue(apiResponse, RequestCBASResponseDTO.class);
        return requestCBASResponseDTO;
    }
}
