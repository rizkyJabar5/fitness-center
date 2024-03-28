package com.jabar.app.fitnesscenter.app.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabar.app.fitnesscenter.app.common.FailureResponse;
import com.jabar.app.fitnesscenter.app.common.exceptions.AppRuntimeException;
import com.jabar.app.fitnesscenter.app.security.SecurityUtils;
import com.jabar.app.fitnesscenter.app.security.jwt.JwtService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.jabar.app.fitnesscenter.app.util.DateConverter.TIMESTAMP_NOW;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
@Slf4j
public class FitnessLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String accessToken = jwtService.getJwtTokenFromHeader(request);
        if (accessToken == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                FailureResponse failureResponse = FailureResponse.builder()
                        .timestamp(TIMESTAMP_NOW)
                        .code(String.valueOf(response.getStatus()))
                        .message("Full authentication is required to access this resource")
                        .path(request.getRequestURI())
                        .build();

                mapper.writeValue(response.getOutputStream(), failureResponse);
            } catch (IOException e) {
                throw new AppRuntimeException(e);
            }
        }

        authentication = SecurityUtils.getAuthentication();
        log.debug("Authentication: {}", authentication);

        if (authentication == null) {
            return;
        }

        log.info("User logged out: " + authentication.getName());

        JWTClaimsSet jwtClaimsSet = jwtService.parseToken(accessToken);
        String username = jwtClaimsSet.getSubject();
        log.info("User from token: " + username);

        if (authentication.getName().equals(username)) {
            SecurityUtils.clearAuthentication();
            SecurityContextHolder.clearContext();
        }
    }
}
