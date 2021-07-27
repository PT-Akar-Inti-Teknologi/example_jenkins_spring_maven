package com.akarinti.preapproved.utils;


import com.akarinti.preapproved.dto.authentication.uidm.logout.UidmLogoutRequestDTO;
import com.akarinti.preapproved.dto.authentication.uidm.logout.UidmLogoutResponseDTO;
import com.akarinti.preapproved.dto.authentication.uidm.userDetail.UserDetailRequestDTO;
import com.akarinti.preapproved.dto.authentication.uidm.userDetail.UserDetailResponseDTO;
import com.akarinti.preapproved.utils.apiresponse.BCAErrorResponse;
import com.akarinti.preapproved.utils.apiresponse.BCAOauth2Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class WebServiceUtil {

    private static String loginUrl;
    @Value("${uidm.login.url}")
    public void setLoginUrl(String loginUrl) {
        WebServiceUtil.loginUrl = loginUrl;
    }

    private static String logoutUrl;
    @Value("${uidm.logout.url}")
    public void setLogoutUrl(String logoutUrl) {
        WebServiceUtil.loginUrl = logoutUrl;
    }

    private static String userDetailUrl;
    @Value("${uidm.user.detail.url}")
    public void setUserDetailUrl(String userDetailUrl) {
        WebServiceUtil.userDetailUrl = userDetailUrl;
    }

    private static String userDetailBySessionIdUrl;
    @Value("${uidm.user.detailBySessionId.url}")
    public void setUserDetailBySessionIdUrl(String userDetailBySessionIdUrl) {
        WebServiceUtil.userDetailBySessionIdUrl = userDetailBySessionIdUrl;
    }

    private static String oauthUrl;
    @Value("${oauth.url}")
    public void setOauthUrl(String oauthUrl) {
        WebServiceUtil.oauthUrl = oauthUrl;
    }

    private static String bcaOauth2ClientId;
    @Value("${oauth.bcaOauth2ClientId}")
    public void setBcaOauth2ClientId(String bcaOauth2ClientId) {
        WebServiceUtil.bcaOauth2ClientId = bcaOauth2ClientId;
    }

    private static String bcaOauth2ClientSecret;
    @Value("${oauth.bcaOauth2ClientSecret}")
    public void setBcaOauth2ClientSecret(String bcaOauth2ClientSecret) {
        WebServiceUtil.bcaOauth2ClientSecret = bcaOauth2ClientSecret;
    }

    private static String apiKey;
    @Value("${oauth.apiKey}")
    public void setApiKey(String apiKey) {
        WebServiceUtil.apiKey = apiKey;
    }

    private static String appKey;
    @Value("${app.access.key}")
    public void setAppKey(String appKey) {
        WebServiceUtil.appKey = appKey;
    }

    private static final Boolean verifySsl = false;

    private static String getBasicAuth(String clientId, String clientSecret) {
        String secret = clientId + ":" + clientSecret;
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        String hash = Base64.getEncoder().encodeToString(secretBytes);
        return "Basic " + hash;
    }

    public static BCAOauth2Response getBCAOauth() {
        String authorization = getBasicAuth(bcaOauth2ClientId, bcaOauth2ClientSecret);

        HttpResponse<BCAOauth2Response> response;
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .post(oauthUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", authorization)
                    .body("grant_type=client_credentials")
                    .asObject(BCAOauth2Response.class);
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + loginUrl);
        }

        return response.getBody();
    }

    @SneakyThrows
    public static String BCAUidmLogin(String accessToken, String timestamp, String signature, Object payload) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
        } catch (Exception e) {
            log.error("ERROR");
        }
        String authorization = "Bearer " + accessToken;
        HttpResponse<JsonNode> response = null;
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .post(loginUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .header("x-bca-key", apiKey)
                    .header("x-bca-timestamp", timestamp)
                    .header("x-bca-signature", signature)
                    .header("app-access-key", appKey)
                    .body(jsonInString)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + loginUrl);
        }
        String apiResponse = response.getBody().toString();
        if (response.getStatus() != 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            BCAErrorResponse bcaErrorResponse = objectMapper.readValue(apiResponse, BCAErrorResponse.class);
            String errorMessage = (String) bcaErrorResponse.getError_message().get("indonesian");
            throw new RuntimeException(errorMessage);
        }
        return apiResponse;
    }

    @SneakyThrows
    public static UidmLogoutResponseDTO BCAUidmLogout(String userId) {
        HttpResponse<JsonNode> response;
        UidmLogoutRequestDTO uidmLogoutRequestDTO = new UidmLogoutRequestDTO();
        uidmLogoutRequestDTO.setUserId(userId);
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .post(logoutUrl)
                    .header("Content-Type", "application/json")
                    .header("app-access-key", appKey)
                    .body(uidmLogoutRequestDTO)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + loginUrl);
        }
        String apiResponse = response.getBody().toString();
        if (response.getStatus() != 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            BCAErrorResponse bcaErrorResponse = objectMapper.readValue(apiResponse, BCAErrorResponse.class);
            String errorMessage = (String) bcaErrorResponse.getError_message().get("indonesian");
            throw new RuntimeException(errorMessage);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(apiResponse, UidmLogoutResponseDTO.class);
    }

    public static String BCAUidmUserDetail(String accessToken, String timestamp, String signature, String userId) {
        return BCAUidmUserDetail(accessToken, timestamp, signature, userId, null);
    }

    public static String BCAUidmUserRoles(String accessToken, String timestamp, String signature, String userId) {
        return BCAUidmUserDetail(accessToken, timestamp, signature, userId, "roles");
    }

    @SneakyThrows
    // TODO: need to check header parameters such as accessToken, timestamp, authorization, and signature
    // TODO: latest version of uidm doc only need app-access-key for header parameter
    private static String BCAUidmUserDetail(String accessToken, String timestamp, String signature, String userId, String subResource) {
        String authorization = "Bearer " + accessToken;
        HttpResponse<JsonNode> response = null;
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .get(userDetailUrl+ userId + (subResource != null ? "/" + subResource : ""))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .header("x-bca-key", apiKey)
                    .header("x-bca-timestamp", timestamp)
                    .header("x-bca-signature", signature)
                    .header("app-access-key", appKey)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + loginUrl);
        }
        String apiResponse = response.getBody().toString();
        if (response.getStatus() != 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            BCAErrorResponse bcaErrorResponse = objectMapper.readValue(apiResponse, BCAErrorResponse.class);
            String errorMessage = (String) bcaErrorResponse.getError_message().get("indonesian");
            throw new RuntimeException(errorMessage);
        }
        return apiResponse;
    }

    @SneakyThrows
    public static UserDetailResponseDTO BCAUidmUserDetailBySessionId(String userIdPic, String sessionId) {
        UserDetailRequestDTO userDetailRequestDTO = new UserDetailRequestDTO();
        userDetailRequestDTO.setLoginSessionId(sessionId);

        HttpResponse<JsonNode> response;
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .post(userDetailBySessionIdUrl)
                    .header("Content-Type", "application/json")
                    .header("app-access-key", appKey)
                    .header("user_id", userIdPic)
                    .body(userDetailRequestDTO)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + loginUrl);
        }
        String apiResponse = response.getBody().toString();
        if (response.getStatus() != 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            BCAErrorResponse bcaErrorResponse = objectMapper.readValue(apiResponse, BCAErrorResponse.class);
            String errorMessage = (String) bcaErrorResponse.getError_message().get("indonesian");
            throw new RuntimeException(errorMessage);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(apiResponse, UserDetailResponseDTO.class);
    }

    public HttpResponse<String> requestPutJackson(String url, String accessToken, Object payload) {
        HttpResponse<String> response;

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(payload);
            String authorization = "Bearer " + accessToken;
            Unirest.config().verifySsl(verifySsl);
            response = Unirest.put(url)
//                    .header("apikey", apikey)
                    .header("authorization", authorization)
                    .header("app-access-key","e4i-1n7-EAI8-Mx3")
                    .header("app-source","EAI-INT")
                    .body(jsonInString)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + url);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Json processing error");
        }
        return response;
    }


}
