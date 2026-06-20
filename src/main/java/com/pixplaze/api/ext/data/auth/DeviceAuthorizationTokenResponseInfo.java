package com.pixplaze.api.ext.data.auth;

public record DeviceAuthorizationTokenResponseInfo(
        String error,
        AuthorizationTokenInfo token
) {}
