package com.akarinti.preapproved.controller;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.authentication.LogoutResponseDTO;
import com.akarinti.preapproved.dto.authentication.SignInRequestDTO;
import com.akarinti.preapproved.dto.authentication.SignInResponseDTO;
import com.akarinti.preapproved.dto.authentication.token.TokenSignInRequestDTO;
import com.akarinti.preapproved.dto.authentication.uidm.logout.UidmLogoutRequestDTO;
import com.akarinti.preapproved.service.SignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    SignInService signInService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> logoutUser(@RequestBody UidmLogoutRequestDTO uidmLogoutRequestDTO) {
        LogoutResponseDTO response = signInService.logout(uidmLogoutRequestDTO.getUserId());
        return ResponseEntity.ok(new ResultDTO(HttpStatus.OK.name(), response));
    }

    @PreAuthorize("permitAll")
    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> loginUser(@RequestBody SignInRequestDTO signInRequestDTO) {
        SignInResponseDTO response = signInService.loginBySession(signInRequestDTO.getSessionId(), signInRequestDTO.getUserId());
        if(response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(new ResultDTO(HttpStatus.OK.name(), response));
    }

    @PostMapping(value = "/test",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> test() {
        return ResponseEntity.ok(new ResultDTO(HttpStatus.OK.name(), "you can see this"));
    }


    @PreAuthorize("permitAll")
    @PostMapping(value = "/generate-token",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> generateToken(@RequestBody TokenSignInRequestDTO signInRequestVO) {
        String response = signInService.generateToken(signInRequestVO);
        if(response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(new ResultDTO(HttpStatus.OK.name(), response));
    }
//
//    @GetMapping(value = "/is-expired",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResultVO> isExpired(@RequestParam(value = "token") String accessToken){
//        AbstractRequestHandler handler = new AbstractRequestHandler() {
//            @Override
//            public Object processRequest() {
//                return signInService.isExpired(accessToken);
//            }
//        };
//
//        return handler.getResult();
//    }
}
