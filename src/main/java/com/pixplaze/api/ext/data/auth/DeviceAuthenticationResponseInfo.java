package com.pixplaze.api.ext.data.auth;

public record DeviceAuthenticationResponseInfo (
        String error,
        AuthorizationTokenInfo token
) {}
