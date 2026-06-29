package com.pixplaze.api.ext.data.auth;

public interface AuthorizationToken {
    String accessToken();
    String refreshToken();
}
