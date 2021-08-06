package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.apiresponse.BCAOauth2Response;
import com.akarinti.preapproved.dto.nlo.RequestCBASPayloadDTO;
import com.akarinti.preapproved.dto.nlo.RequestCBASResponseDTO;
import com.akarinti.preapproved.dto.apiresponse.BCAErrorResponse;
import com.akarinti.preapproved.utils.WebServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
        BCAOauth2Response bcaOauth2Response = WebServiceUtil.getBCAOauth();
        String authorization = "Bearer " + bcaOauth2Response.getAccess_token();

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = null;
        try {
            jsonBody = mapper.writeValueAsString(requestCBASPayloadDTO);
        } catch (Exception e) {
            log.error("ERROR");
        }
        HttpResponse<JsonNode> response = null;
        try {
            Unirest.config().verifySsl(WebServiceUtil.getVerifySSL());
            response = Unirest
                    .post(cbasUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .header("app-access-key", appAccessKey)
                    .header("app-source", appSource)
                    .body(jsonBody)
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
        return objectMapper.readValue(apiResponse, RequestCBASResponseDTO.class);
    }
}
