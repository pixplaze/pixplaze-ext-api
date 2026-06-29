package com.pixplaze.api.ext.data.auth;

public record VerifiableAuthorizationTokenInfo(
        String accessToken,
        String refreshToken,
        String publicKey
) implements AuthorizationToken {}