package com.jabar.app.fitnesscenter.app.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabar.app.fitnesscenter.app.common.FailureResponse;
import com.jabar.app.fitnesscenter.app.common.exceptions.AppRuntimeException;
import com.jabar.app.fitnesscenter.app.security.SecurityUtils;
import com.jabar.app.fitnesscenter.app.security.jwt.JwtService;
import com.jabar.app.fitnesscenter.feature.user.entity.dto.LoginRequest;
import com.jabar.app.fitnesscenter.feature.user.entity.model.AppUser;
import com.jabar.app.fitnesscenter.feature.user.service.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.AUTHENTICATION_URL;
import static com.jabar.app.fitnesscenter.app.constant.SecurityConst.TOKEN_PREFIX;
import static com.jabar.app.fitnesscenter.app.util.DateConverter.TIMESTAMP_NOW;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class FitnessAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtService jwtService;
    private final AppUserService appUserService;

    public FitnessAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager, AppUserService appUserService) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.appUserService = appUserService;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(AUTHENTICATION_URL + "/signin", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        var objectMapper = new ObjectMapper();
        try {
            var loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            log.debug("Login with username: {}", loginRequest.username());

            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        var appUser = (AppUser) authResult.getPrincipal();
        var generatedJwtToken = jwtService.generateJwtToken(appUser, appUser.getAuthorities(), request.getServletPath());
        SecurityUtils.authenticateUserWithoutCredentials(request, appUser, appUser.getAuthorities());

        // SET AND SAVE REFRESH TOKEN TO DB
        appUser.setRefreshToken(generatedJwtToken.refreshToken());
        appUser.setExpiresRefreshToken(new Date(System.currentTimeMillis() + generatedJwtToken.expirationRefreshToken()));
        appUserService.updateAppUser(appUser);
        //
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), generatedJwtToken);
        response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + generatedJwtToken.accessToken());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        FailureResponse failureResponse = FailureResponse.builder()
                .timestamp(TIMESTAMP_NOW)
                .code(String.valueOf(response.getStatus()))
                .message(failed.getMessage())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), failureResponse);
    }
}