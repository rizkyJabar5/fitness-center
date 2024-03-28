package com.jabar.app.fitnesscenter.feature.user.entity.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record JwtResponse(
        String userId,
        String username,
        String accessToken,
        Long expirationAccessToken,
        String refreshToken,
        Long expirationRefreshToken,
        String type) implements Serializable {

//    /**
//     * Build jwtResponse object from the specified userDetails.
//     *
//     * @param jwToken     the jwToken.
//     * @return the jwtResponse object.
//     */
//    public static JwtResponse buildJwtResponse(String jwToken, String refreshToken) {
//        var appUser = SecurityUtils.getAuthenticatedUserDetails();
//        app
//
//        return JwtResponse.builder()
//                .accessToken(jwToken)
//                .refreshToken(refreshToken)
//                .subject(appUser.getUsername())
//                .username(appUser.getUsername())
//                .type(TOKEN_PREFIX)
//                .role(appUser.getAuthorities().stream().map(ls ->ls.getAuthority()))
//                .build();
//    }
}
