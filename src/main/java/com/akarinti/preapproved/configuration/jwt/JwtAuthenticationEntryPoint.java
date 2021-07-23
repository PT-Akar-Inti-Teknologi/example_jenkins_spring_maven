package com.akarinti.preapproved.configuration.jwt;

import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.utils.exception.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        final ResultDTO resultDTO = new ResultDTO(StatusCode.UNAUTHORIZED, null);
        ObjectMapper mapper = new ObjectMapper();
        String responseMsg = mapper.writeValueAsString(resultDTO);

        response.getWriter().write(responseMsg);
    }
}
