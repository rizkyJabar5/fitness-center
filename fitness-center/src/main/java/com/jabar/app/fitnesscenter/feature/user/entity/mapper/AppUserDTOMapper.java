//package com.jabar.app.fitnesscenter.feature.user.entity.mapper;
//
//import com.jabar.app.fitnesscenter.feature.user.entity.model.AppUser;
//import com.jabar.app.fitnesscenter.feature.user.entity.dto.AppUserDto;
//import org.apache.commons.lang3.Validate;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@Component
//public class AppUserDTOMapper implements Function<AppUser, AppUserDto> {
//    @Override
//    public AppUserDto apply(AppUser appUser) {
//        Validate.notNull(appUser, "User must not be null");
//
//        return AppUserDto.builder()
//                .userId(appUser.getUserId())
//                .email(appUser.getEmail())
//                .role(appUser.getERole().getRoleName())
//                .otpRequestedTime(appUser.getOtpRequestedTime())
//                .isCredentialsNonExpired(appUser.isCredentialsNonExpired())
//                .isEnabled(appUser.isEnabled())
//                .build();
//    }
//}
