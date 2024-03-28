package com.jabar.app.fitnesscenter.app.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.app.common.FailureResponse;
import com.jabar.app.fitnesscenter.app.constant.OperationStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.jabar.app.fitnesscenter.app.util.DateConverter.TIMESTAMP_NOW;

@Component
@Slf4j
public class FitnessLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();

        if (authentication == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            FailureResponse failureResponse = FailureResponse.builder()
                    .timestamp(TIMESTAMP_NOW)
                    .message("You're not logged in")
                    .code(String.valueOf(response.getStatus()))
                    .path(request.getServletPath())
                    .build();
            objectMapper.writeValue(response.getOutputStream(), failureResponse);
            return;
        }

        log.info("User logged out: " + authentication.getName());

        BaseResponse<Void> baseResponse = BaseResponse.<Void>builder()
                .responseCode(String.valueOf(HttpStatus.OK.value()))
                .responseMessage(OperationStatus.SUCCESS.getName() + " to logout")
                .build();
        objectMapper.writeValue(response.getOutputStream(), baseResponse);
    }
}
