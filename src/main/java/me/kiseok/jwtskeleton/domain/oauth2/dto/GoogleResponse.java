package me.kiseok.jwtskeleton.domain.oauth2.dto;

import lombok.Data;

@Data
public class GoogleResponse {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String idToken;
}
