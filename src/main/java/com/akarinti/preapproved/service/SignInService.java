package com.akarinti.preapproved.service;

import com.akarinti.preapproved.configuration.jwt.SessionAuthenticationProvider;
import com.akarinti.preapproved.configuration.jwt.TokenProvider;
import com.akarinti.preapproved.dto.authentication.ProfileUserDTO;
import com.akarinti.preapproved.dto.authentication.SignInRequestDTO;
import com.akarinti.preapproved.dto.authentication.SignInResponseDTO;
import com.akarinti.preapproved.jpa.repository.UserBCARepository;
import com.akarinti.preapproved.utils.WebServiceUtil;
import com.akarinti.preapproved.utils.apiresponse.BCAOauth2Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

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


    public SignInResponseDTO loginBySession(String sessionId) {
        //dummy, will be replaced with a real one
        if(!sessionId.equals("dummy")){
            return null;
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
        signInResponseVO.setRoles(Collections.singletonList("Supervisor"));

        return signInResponseVO;
    }

    public SignInResponseDTO logout(String sessionId) {
        //dummy, will be replaced with a real one
        if(!sessionId.equals("dummy")){
            return null;
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
        signInResponseVO.setRoles(Collections.singletonList("Supervisor"));

        return signInResponseVO;
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

}
