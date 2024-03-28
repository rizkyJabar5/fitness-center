package com.jabar.app.fitnesscenter.feature.user.service;


import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.app.common.exceptions.AppRuntimeException;
import com.jabar.app.fitnesscenter.app.common.exceptions.ResourceNotFoundException;
import com.jabar.app.fitnesscenter.app.util.DateConverter;
import com.jabar.app.fitnesscenter.feature.email.EmailService;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.StatusMembership;
import com.jabar.app.fitnesscenter.feature.user.entity.dto.PasswordRequest;
import com.jabar.app.fitnesscenter.feature.user.entity.model.AppUser;
import com.jabar.app.fitnesscenter.feature.user.entity.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.Date;

import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.messageFormat;
import static com.jabar.app.fitnesscenter.app.constant.UserConstant.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppUserServiceImpl implements UserDetailsService, AppUserService {
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000L; // 5 minutes
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case-sensitive, or case-insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getAppUserByUsername(username);
    }

    @Transactional
    @Override
    public String createNewUser(Member member, String password) {
        AppUser user = new AppUser();
        user.setMember(member);
        user.setPassword(passwordEncoder.encode(password));
        user.setIsEnabled(false);
        user.setIsCredentialsNonExpired(true);
        user.setOtpRequestedTime(new Date(System.currentTimeMillis()));
        String otp = RandomStringUtils.randomNumeric(6);
        user.setOtpCode(Long.parseLong(otp));

        userRepository.save(user);

        return otp;
    }

    @Override
    public BaseResponse<Void> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        AppUser persistent = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token is not found"));

        LocalDate localDate = LocalDate.now().plusDays(3);
        Date date = DateConverter.toDate(localDate);
        persistent.setExpiresRefreshToken(date);
        userRepository.save(persistent);

        return new BaseResponse<>(HttpStatus.OK.value(), "Success to Refresh Token");
    }

    @Transactional
    @Override
    public BaseResponse<Void> forgetPassword(String email) {
        String otpCode = RandomStringUtils.randomNumeric(6);
        AppUser appUser = getAppUserByUsername(email);
        appUser.setOtpCode(Long.parseLong(otpCode));
        appUser.setOtpRequestedTime(new Date());
        userRepository.save(appUser);

        emailService.sendEmailFromHtmlTemplate(email,
                "Your OTP for Password Reset",
                "forget-password",
                forgetPasswordcontext(otpCode));

        return new BaseResponse<>(HttpStatus.OK.value(), "Success to request password reset");
    }

    @Transactional
    @Override
    public BaseResponse<Void> resetPassword(String email, String otpCode, PasswordRequest request) {
        AppUser appUser = getAppUserByUsername(email);
        assertSameUser(email, appUser);
        assertValidOTP(otpCode, appUser);

        boolean matches = passwordEncoder.matches(request.oldPassword(), appUser.getPassword());
        if (!matches) {
            throw new AppRuntimeException("Password did not match");
        }

        boolean passwordNewMatchWithOldPassword = passwordEncoder.matches(request.newPassword(), appUser.getPassword());
        if (passwordNewMatchWithOldPassword) {
            throw new AppRuntimeException("Please enter another password!");
        }

        appUser.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(appUser);

        return new BaseResponse<>(HttpStatus.OK.value(), "Success to change your password");
    }

    @Override
    public BaseResponse<Void> verifyOTP(String username, String otpCode) {
        AppUser appUser = getAppUserByUsername(username);

        if (appUser.isEnabled()) {
            return new BaseResponse<>(
                    HttpStatus.OK.value(),
                    "Account is verified");
        }

        assertValidOTP(otpCode, appUser);

        appUser.setIsEnabled(true);
        appUser.getMember().setStatusMembership(StatusMembership.REGISTERED);
        userRepository.save(appUser);

        return new BaseResponse<>(
                HttpStatus.OK.value(),
                "Email is activated");
    }

    private void assertValidOTP(String otpCode, AppUser appUser) {
        long otpCodeParse = Long.parseLong(otpCode);
        if (otpCodeParse != appUser.getOtpCode()) {
            throw new AppRuntimeException("OTP is not valid");
        }

        boolean validOTP = assertValidOTP(appUser.getOtpRequestedTime());
        if (!validOTP) {
            throw new AppRuntimeException("OTP is expired");
        }
    }

    @Override
    public AppUser getAppUserById(String userId) {
        String message = messageFormat(USER_NOT_FOUND_MSG, userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(message));
    }

    @Override
    public void updateAppUser(AppUser appUser) {
        this.getAppUserByUsername(appUser.getUsername());

        userRepository.save(appUser);
    }

    private Context forgetPasswordcontext(String otpCode) {
        Context context = new Context();
        context.setVariable("otp-code", otpCode);
        return context;
    }

    private AppUser getAppUserByUsername(String username) {
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(EMAIL_OR_USERNAME_NOT_PROVIDED_MSG));
    }

    private boolean assertValidOTP(Date otpRequestedTime) {
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = otpRequestedTime.getTime();

        return otpRequestedTimeInMillis + OTP_VALID_DURATION >= currentTimeInMillis;
    }

    private void assertSameUser(String email, AppUser appUser) {
        if (!email.equals(appUser.getUsername())) {
            throw new DataIntegrityViolationException(NOT_MATCH_EMAIL);
        }
    }
}
