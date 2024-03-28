package com.jabar.app.fitnesscenter.feature.user.entity.dto;

import com.jabar.app.fitnesscenter.feature.user.entity.model.AppUser;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link AppUser}
 */
@Builder
public record AppUserDto(
        String userId,
        String name,
        String email,
        String role,
        Date otpRequestedTime,
        Boolean isCredentialsNonExpired,
        Boolean isEnabled) implements Serializable {
}