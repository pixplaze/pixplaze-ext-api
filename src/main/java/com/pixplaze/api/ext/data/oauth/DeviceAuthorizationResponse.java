package com.pixplaze.api.ext.data.oauth;

import io.soabase.recordbuilder.core.RecordBuilder;

/// Device authorization response (RFC 8628 §3.2): returned by the authorization endpoint
/// to start the device flow. On the wire the field names are snake_case
/// (`device_code`, `user_code`, `expires_in`, `verification_uri_complete`, …).
///
/// @param deviceCode              secret code the device polls the token endpoint with
/// @param userCode                code the user enters (or confirms) on the verification page
/// @param expiresIn               lifetime of both codes, seconds
/// @param interval                minimal pause between token polls, seconds
/// @param verificationUri         page where the user confirms the authorization
/// @param verificationUriComplete [#verificationUri] with the [#userCode] already filled in
@RecordBuilder
public record DeviceAuthorizationResponse(
        String deviceCode,
        String userCode,
        Long expiresIn,
        Long interval,
        String verificationUri,
        String verificationUriComplete
) implements DeviceAuthorizationResponseBuilder.With {

    /// Builder entry point: `DeviceAuthorizationResponse.builder()...build()`.
    public static DeviceAuthorizationResponseBuilder builder() {
        return DeviceAuthorizationResponseBuilder.builder();
    }

    /// Builder pre-filled with a copy of `from`.
    public static DeviceAuthorizationResponseBuilder builder(DeviceAuthorizationResponse from) {
        return DeviceAuthorizationResponseBuilder.builder(from);
    }
}
