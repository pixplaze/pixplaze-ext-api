package com.pixplaze.api.ext.data.auth;

/// Token pair issued on Minecraft server registration, together with the key to verify tokens.
///
/// @param accessToken  JWT signed with ES256
/// @param refreshToken opaque, single-use
/// @param publicKey    Base64 DER of the public signing key
public record VerifiableAuthorizationTokenInfo(
        String accessToken,
        String refreshToken,
        String publicKey
) implements AuthorizationToken {}
