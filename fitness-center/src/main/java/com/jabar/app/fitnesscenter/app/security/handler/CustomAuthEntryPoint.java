/*
 * Copyright (c) 2022.
 */

package com.jabar.app.fitnesscenter.app.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabar.app.fitnesscenter.app.common.FailureResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.jabar.app.fitnesscenter.app.util.DateConverter.TIMESTAMP_NOW;

@Component
@Slf4j
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        FailureResponse failureResponse = FailureResponse.builder()
                .timestamp(TIMESTAMP_NOW)
                .message(authException.getMessage())
                .code(String.valueOf(response.getStatus()))
                .path(request.getServletPath())
                .build();

        log.error("Unauthorized error: {}", authException.getMessage());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), failureResponse);
    }
}
