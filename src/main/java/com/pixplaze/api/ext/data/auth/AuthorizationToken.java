package com.pixplaze.api.ext.data.auth;

/// Access and refresh token pair. The access token is a JWT signed with ES256: its lifetime is the
/// `exp` claim.
public interface AuthorizationToken {
    String accessToken();
    String refreshToken();
}
