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
import com.akarinti.preapproved.jpa.entity.UserBCA;
import com.akarinti.preapproved.jpa.repository.UserBCARepository;
import com.akarinti.preapproved.utils.WebServiceUtil;
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

        Set<GrantedAuthority> authorities = new HashSet<>();
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

            assert userDetailResponseDTO != null;
            final Authentication authentication = setUserAuthentication(userDetailResponseDTO, sessionId);

            String newAccessToken = tokenProvider.generateToken(authentication);

            ProfileUserDTO profileUserDTO = userDetailResponseDTO.getUserDetail();

            SignInResponseDTO signInResponseVO = new SignInResponseDTO();
            signInResponseVO.setAccessToken(newAccessToken);
            signInResponseVO.setProfileUserInternal(profileUserDTO);
            signInResponseVO.setRoles(Collections.singletonList(profileUserDTO.getRoleCode().toUpperCase()));

            setUserBCA(profileUserDTO, sessionId);

            return signInResponseVO;
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
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

    @Transactional
    public void setUserBCA(ProfileUserDTO profileUserDTO, String sessionId) {
        UserBCA userBCA = userBCARepository.findByEmail(profileUserDTO.getEmail());
        if(userBCA == null){
            userBCA = new UserBCA();
        }
        userBCA.setUserIdPic(profileUserDTO.getUserId());
        userBCA.setFullName((profileUserDTO.getFullName() != null) ? profileUserDTO.getFullName() : profileUserDTO.getUserName());
        userBCA.setEmail(profileUserDTO.getEmail());
        userBCA.setRole(profileUserDTO.getRoleCode());

        String password = passwordEncoder.encode(sessionId);
        userBCA.setPassword(password);
        userBCA.setPhoneNumber(profileUserDTO.getPhoneNumber());
        userBCA.setOfficeCode(profileUserDTO.getOfficeCode());
        userBCA.setOfficeName(profileUserDTO.getOfficeName());
        userBCA.setDeptCode(profileUserDTO.getDeptCode());
        userBCA.setDeptName(profileUserDTO.getDeptName());
        userBCA.setSubDeptCode(profileUserDTO.getSubDeptCode());
        userBCA.setSubDeptName(profileUserDTO.getSubDeptName());
        userBCA.setJobTitleCode(profileUserDTO.getJobTitleCode());
        userBCA.setJobTitleName(profileUserDTO.getJobTitleName());
        userBCA.setAppCode(profileUserDTO.getAppCode());
        userBCA.setMobileNumber(profileUserDTO.getMobileNumber());

        userBCARepository.saveAndFlush(userBCA);
    }


    public LogoutResponseDTO logout(String userId) {
        UidmLogoutResponseDTO uidmLogoutResponseDTO = WebServiceUtil.BCAUidmLogout(userId);
        LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO();
        Boolean status = uidmLogoutResponseDTO.getLogoutStatus().equalsIgnoreCase("1");
        logoutResponseDTO.setStatus(status);
        return logoutResponseDTO;
    }

    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new CustomException(StatusCode.UNAUTHORIZED);
        }
        return authentication.getName();
    }

    public UserBCA getUser(){
        return userBCARepository.findByEmail(getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserBCA user = userBCARepository.findByEmail(s);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getPrivileges(user));
    }

    private static Set<SimpleGrantedAuthority> getPrivileges(UserBCA user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    public GenerateTokenResponseDTO generateToken(TokenSignInRequestDTO signInRequest){
        if (signInRequest.getUsername().equals(tokenUsername) && signInRequest.getPassword().equals(tokenPassword)) {
            Set<GrantedAuthority> authorities = new HashSet<>();
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
