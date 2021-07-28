package com.akarinti.preapproved.service;

import com.akarinti.preapproved.configuration.jwt.SessionAuthenticationProvider;
import com.akarinti.preapproved.configuration.jwt.TokenProvider;
import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import com.akarinti.preapproved.dto.authentication.LogoutResponseDTO;
import com.akarinti.preapproved.dto.authentication.ProfileUserDTO;
import com.akarinti.preapproved.dto.authentication.SignInResponseDTO;
import com.akarinti.preapproved.dto.authentication.token.GenerateTokenResponseDTO;
import com.akarinti.preapproved.dto.authentication.token.TokenSignInRequestDTO;
import com.akarinti.preapproved.dto.authentication.uidm.logout.UidmLogoutResponseDTO;
import com.akarinti.preapproved.dto.authentication.uidm.userDetail.UserDetailResponseDTO;
import com.akarinti.preapproved.jpa.repository.UserBCARepository;
import com.akarinti.preapproved.utils.WebServiceUtil;
import com.akarinti.preapproved.dto.apiresponse.BCAOauth2Response;
import com.akarinti.preapproved.utils.exception.CustomException;
import com.akarinti.preapproved.utils.exception.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
public class SignInService implements UserDetailsService {

    @Autowired
    @Qualifier("passwordEncoder")
    PasswordEncoder passwordEncoder;

    @Autowired
    UserBCARepository userBCARepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    SessionAuthenticationProvider sessionAuthenticationProvider;

    private static String httpMethod;
    @Value("${oauth.httpMethod}")
    public void setHttpMethod(String httpMethod) {
        SignInService.httpMethod = httpMethod;
    }

    private static String requestBody;
    @Value("${oauth.requestBody}")
    public void setRequestBody(String requestBody) {
        SignInService.requestBody = requestBody;
    }

    private static String apiSecret;
    @Value("${oauth.apiSecret}")
    public void setApiSecret(String apiSecret) {
        SignInService.apiSecret = apiSecret;
    }

    @Value("${token.username}")
    String tokenUsername;

    @Value("${token.password}")
    String tokenPassword;

    private Authentication setUserAuthentication(UserDetailResponseDTO userDetailResponseDTO, String sessionId) {
        ProfileUserDTO userDTO = userDetailResponseDTO.getUserDetail();

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(userDTO.getRoleCode()));

        User userPrincipal = new User(userDTO.getUserId(), "", true, true, true, true, authorities);
        // FIXME: check UsernamePasswordAuthenticationToken credential params, do we need to use sessionId?
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, sessionId, authorities);
        return sessionAuthenticationProvider.authenticate(authenticationToken);
    }

    public SignInResponseDTO loginBySession(String sessionId, String userId) {
        if(!sessionId.equals("dummy")){
            UserDetailResponseDTO userDetailResponseDTO = null;
            try {
                userDetailResponseDTO = WebServiceUtil.BCAUidmUserDetailBySessionId(userId, sessionId);
            } catch (JsonProcessingException e) {
                log.error("exception: "+ e);
            }

            final Authentication authentication = setUserAuthentication(userDetailResponseDTO, sessionId);

            String newAccessToken = tokenProvider.generateToken(authentication);

            ProfileUserDTO profileUserDTO = userDetailResponseDTO.getUserDetail();

            SignInResponseDTO signInResponseVO = new SignInResponseDTO();
            signInResponseVO.setAccessToken(newAccessToken);
            signInResponseVO.setProfileUserInternal(profileUserDTO);
            // FIXME: need to check role later
            signInResponseVO.setRoles(Collections.singletonList("VERIFIER"));

            return signInResponseVO;
        }

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User userPrincipal = new User(sessionId, "", true, true, true, true, authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, sessionId, authorities);
        final Authentication authentication = sessionAuthenticationProvider.authenticate(authenticationToken);

        String newAccessToken = tokenProvider.generateToken(authentication);

        ProfileUserDTO profileUserDTO = new ProfileUserDTO();
        profileUserDTO.setUserName("dummy");
        profileUserDTO.setEmail("dummy@dummy.com");
        profileUserDTO.setFullName("Dummy");

        SignInResponseDTO signInResponseVO = new SignInResponseDTO();
        signInResponseVO.setAccessToken(newAccessToken);
        signInResponseVO.setProfileUserInternal(profileUserDTO);
        signInResponseVO.setRoles(Collections.singletonList("VERIFIER"));

        return signInResponseVO;
    }

    public LogoutResponseDTO logout(String userId) {
        UidmLogoutResponseDTO uidmLogoutResponseDTO = WebServiceUtil.BCAUidmLogout(userId);
        LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO();
        Boolean status = uidmLogoutResponseDTO.getLogoutStatus().equalsIgnoreCase("1");
        logoutResponseDTO.setStatus(status);
        return logoutResponseDTO;
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
        String stringToSign = httpMethod + ":" + relativeUrl + ":" + accessToken + ":" + requestBodyHash + ":" + timestamp;
        return stringToSign;
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
//        log.info("PRINTING SIGNATURE PAYLOAD=================================================");
//        System.out.println(result);
//        log.info("===========================================================================");
        return result;
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        UserBCA user = userBCARepository.findByEmailAndDeletedFalse(s);
//        if(user == null){
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getPrivileges(user));
//    }

    public GenerateTokenResponseDTO generateToken(TokenSignInRequestDTO signInRequest){
        if (signInRequest.getUsername().equals(tokenUsername) && signInRequest.getPassword().equals(tokenPassword)) {
            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ACCESS_RUMAHSAYA_SERVICES"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword(), authorities);
            final Authentication authentication = sessionAuthenticationProvider.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = tokenProvider.generatePublicToken(authentication);
            GenerateTokenResponseDTO generateTokenResponseDTO = new GenerateTokenResponseDTO();
            generateTokenResponseDTO.setAccessToken(accessToken);
            return generateTokenResponseDTO;
        } else {
            log.error("message: "+ StatusCode.UNAUTHORIZED.message());
            throw new CustomException(StatusCode.UNAUTHORIZED, new StatusCodeMessageDTO("tidak punya akses", "unauthorized"));
        }
    }
}
