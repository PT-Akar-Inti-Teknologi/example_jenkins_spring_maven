package com.akarinti.preapproved.utils;


import com.akarinti.preapproved.dto.authentication.uidm.logout.UidmLogoutRequestDTO;
import com.akarinti.preapproved.dto.authentication.uidm.logout.UidmLogoutResponseDTO;
import com.akarinti.preapproved.dto.authentication.uidm.userDetail.UserDetailResponseDTO;
import com.akarinti.preapproved.dto.apiresponse.BCAErrorResponse;
import com.akarinti.preapproved.dto.apiresponse.BCAOauth2Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kong.unirest.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

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
        WebServiceUtil.logoutUrl = logoutUrl;
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

    private static String apiSecret;
    @Value("${oauth.apiSecret}")
    public void setApiSecret(String apiSecret) {
        WebServiceUtil.apiSecret = apiSecret;
    }

    private static String oauthHttpMethod;
    @Value("${oauth.httpMethod}")
    public void setOauthHttpMethod(String oauthHttpMethod) {
        WebServiceUtil.oauthHttpMethod = oauthHttpMethod;
    }

    private static String appKey;
    @Value("${app.access.key}")
    public void setAppKey(String appKey) {
        WebServiceUtil.appKey = appKey;
    }

    private static String requestBody;
    @Value("${oauth.requestBody}")
    public void setRequestBody(String requestBody) {
        WebServiceUtil.requestBody = requestBody;
    }

    private static final Boolean verifySsl = false;

    public static boolean getVerifySSL() {
        return verifySsl;
    }

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
        BCAOauth2Response bcaOauth2Response = WebServiceUtil.getBCAOauth();
        String authorization = "Bearer " + bcaOauth2Response.getAccess_token();
        HttpResponse<JsonNode> response;
        UidmLogoutRequestDTO uidmLogoutRequestDTO = new UidmLogoutRequestDTO();
        uidmLogoutRequestDTO.setUserId(userId);

        String jsonBody = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonBody = mapper.writeValueAsString(uidmLogoutRequestDTO);
        } catch (Exception e) {
            log.error("ERROR");
        }
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .post(logoutUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .header("app-access-key", appKey)
                    .body(jsonBody)
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

    private static String getTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date dt = new Date();

        String date = dateFormat.format(dt);
        String literal = "T";
        String time = timeFormat.format(dt);
        String millisecond = String.valueOf(System.currentTimeMillis());
        String SSS = millisecond.substring(10);
        String tzd = "+07:00";

        String timestamp = date + literal + time + "." + SSS + tzd;

        return timestamp;
    }

    private static String getRequestBodyHash() {
        StringBuilder result = new StringBuilder("");
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(requestBody.getBytes(StandardCharsets.UTF_8));

            for(byte hex : digest) {
                String hexFormat = String.format("%02x", hex);
                result.append(hexFormat);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static String getRelativeUrl() {
        String relativeUrl = "/uidm/general/api/myPARTNER/login";
        return relativeUrl;
    }

    private static String getStringToSign(String accessToken, String timestamp) {
        String requestBodyHash = getRequestBodyHash();
        String relativeUrl = getRelativeUrl();
        String stringToSign = oauthHttpMethod + ":" + relativeUrl + ":" + accessToken + ":" + requestBodyHash + ":" + timestamp;
        return stringToSign;
    }

    private static String getHmacSha256Hash(String stringToSign) {
        StringBuilder result = new StringBuilder("");
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            byte[] secretKeyBytes = apiSecret.getBytes(StandardCharsets.UTF_8);
            byte[] messageBytes = stringToSign.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] hmacSHA256Bytes = hmacSHA256.doFinal(messageBytes);

            for(byte hex : hmacSHA256Bytes) {
                String hexFormat = String.format("%02x", hex);
                result.append(hexFormat);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static HashMap<String, String> getSignatureInfo() {
        HashMap<String, String> result = new HashMap<String, String>();

        BCAOauth2Response bcaOauth2Response = WebServiceUtil.getBCAOauth();
        String accessToken = bcaOauth2Response.getAccess_token();

        String timestamp = getTimestamp();
        String stringToSign = getStringToSign(accessToken, timestamp);
        String signature = getHmacSha256Hash(stringToSign);

        result.put("stringToSign", stringToSign);
        result.put("signature", signature);
        result.put("accessToken", accessToken);
        result.put("timestamp", timestamp);
        result.put("status", "OK");
        return result;
    }

    public static UserDetailResponseDTO BCAUidmUserDetailBySessionId(String userIdPic, String sessionId) throws JsonProcessingException { ;
        BCAOauth2Response bcaOauth2Response = getBCAOauth();
        String authorization = "Bearer " + bcaOauth2Response.getAccess_token();

        final String API_URL = userDetailBySessionIdUrl;

        HttpResponse<JsonNode> response;
        try {
            Unirest.config().verifySsl(verifySsl);
            response = Unirest
                    .get(API_URL)
                    .header("Authorization", authorization)
                    .header("app-access-key", appKey)
                    .header("user-id", userIdPic)
                    .queryString("login-session-id", sessionId)
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

        ObjectNode node = new ObjectMapper().readValue(apiResponse, ObjectNode.class);

        String profileInternal = node.get("output_schema").toString();
        return objectMapper.readValue(profileInternal, UserDetailResponseDTO.class);
    }
}
