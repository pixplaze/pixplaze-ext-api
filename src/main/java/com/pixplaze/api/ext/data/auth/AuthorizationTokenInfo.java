package com.pixplaze.api.ext.data.auth;

public record AuthorizationTokenInfo(
        String accessToken,
        String refreshToken
) implements AuthorizationToken {
    public AuthorizationTokenInfo safe() {
        return new AuthorizationTokenInfo(accessToken, null);
    }
}
