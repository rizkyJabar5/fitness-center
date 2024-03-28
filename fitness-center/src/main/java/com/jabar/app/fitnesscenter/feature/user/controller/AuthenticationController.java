package com.jabar.app.fitnesscenter.feature.user.controller;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MemberDto;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;
import com.jabar.app.fitnesscenter.feature.membership.service.MembershipService;
import com.jabar.app.fitnesscenter.feature.user.entity.dto.PasswordRequest;
import com.jabar.app.fitnesscenter.feature.user.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.AUTHENTICATION_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(AUTHENTICATION_URL)
public class AuthenticationController {
    private final AppUserService userService;
    private final MembershipService membershipService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<MemberDto>> registerMembership(@Valid @RequestBody MembershipDetailsRequest request) {
        BaseResponse<MemberDto> baseResponse = membershipService.registration(request);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<BaseResponse<Void>> refreshToken(HttpServletRequest request) {
        BaseResponse<Void> baseResponse = userService.refreshToken(request);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/verify-registration")
    public ResponseEntity<BaseResponse<Void>> verifyEmail(@RequestParam String username,
                                                          @RequestParam(name = "otp_code") String otpCode) {
        BaseResponse<Void> response = userService.verifyOTP(username, otpCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<BaseResponse<Void>> forgetPassword(@Valid @RequestParam String email) {
        BaseResponse<Void> baseResponse = userService.forgetPassword(email);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/reset-password/{email}")
    public ResponseEntity<BaseResponse<Void>> resetPassword(
            @PathVariable String email,
            @RequestParam(name = "otp_code") String otpCode,
            @Valid @RequestBody PasswordRequest request) {
        BaseResponse<Void> baseResponse = userService.resetPassword(email, otpCode, request);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
