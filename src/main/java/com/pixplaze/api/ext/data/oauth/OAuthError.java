package com.pixplaze.api.ext.data.oauth;

/// Error codes of the authorization and token endpoints (RFC 8628 §3.5, RFC 6749 §5.2).
public enum OAuthError {
    /// The user has not decided yet: wait [DeviceAuthorizationResponse#interval] and poll again.
    AUTHORIZATION_PENDING("authorization_pending"),
    /// Polling too often: increase the pause and poll again.
    SLOW_DOWN("slow_down"),
    /// The user denied the authorization, or the subject's checks failed: stop.
    ACCESS_DENIED("access_denied"),
    /// The device code expired, the poll budget ran out, or the tokens were not picked up in time:
    /// start the flow over.
    EXPIRED_TOKEN("expired_token"),
    /// A required parameter is missing, or the authorization details are malformed or invalid.
    INVALID_REQUEST("invalid_request"),
    /// The scope cannot be parsed or is not supported.
    INVALID_SCOPE("invalid_scope"),
    /// The client id does not match the session, the decision was already made,
    /// or the refresh token is invalid.
    INVALID_GRANT("invalid_grant"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type"),
    /// Internal error: retry later.
    SERVER_ERROR("server_error");

    private final String code;

    OAuthError(String code) {
        this.code = code;
    }

    /// Wire code, e.g. `"authorization_pending"`.
    public String code() {
        return code;
    }

    /// Parses a wire code; returns `null` for an unknown or `null` code.
    public static OAuthError of(String code) {
        for (var error : values()) {
            if (error.code.equals(code)) {
                return error;
            }
        }
        return null;
    }
}
