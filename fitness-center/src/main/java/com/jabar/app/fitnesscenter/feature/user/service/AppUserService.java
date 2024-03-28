package com.jabar.app.fitnesscenter.feature.user.service;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.user.entity.dto.PasswordRequest;
import com.jabar.app.fitnesscenter.feature.user.entity.model.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    String createNewUser(Member member, String password);

    BaseResponse<Void> refreshToken(HttpServletRequest request);

    BaseResponse<Void> forgetPassword(String email);

    BaseResponse<Void> resetPassword(String email, String otpCode, PasswordRequest request);

    BaseResponse<Void> verifyOTP(String username, String otpCode);

    AppUser getAppUserById(String userId);

    void updateAppUser(AppUser appUser);
}
