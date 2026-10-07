package com.pixplaze.api.ext.data.oauth;

/// Error response of the authorization and token endpoints (RFC 6749 §5.2). On the wire the field
/// names are snake_case (`error`, `error_description`).
///
/// @param error            wire code, see [OAuthError]
/// @param errorDescription human-readable details; optional
public record OAuthErrorResponse(
        String error,
        String errorDescription
) {
    public static OAuthErrorResponse of(OAuthError error) {
        return new OAuthErrorResponse(error.code(), null);
    }

    /// Parsed [#error]; `null` if the code is unknown to this version of the library.
    public OAuthError oauthError() {
        return OAuthError.of(error);
    }
}
