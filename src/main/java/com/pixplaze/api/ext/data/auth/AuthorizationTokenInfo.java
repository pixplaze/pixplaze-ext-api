package com.pixplaze.api.ext.data.auth;

/// Token pair issued by application sign-up, sign-in and refresh, and by the device flow
/// of a Minecraft player and of the application.
///
/// @param accessToken  JWT signed with ES256
/// @param refreshToken opaque, single-use
public record AuthorizationTokenInfo(
        String accessToken,
        String refreshToken
) implements AuthorizationToken {
    /// Copy without the refresh token: for responses where it travels in a cookie.
    public AuthorizationTokenInfo safe() {
        return new AuthorizationTokenInfo(accessToken, null);
    }
}
