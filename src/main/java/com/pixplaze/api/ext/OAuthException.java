package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.oauth.OAuthError;
import com.pixplaze.api.ext.data.oauth.OAuthErrorResponse;

/// OAuth error returned by the authorization or token endpoint, see [OAuthError] for what to do
/// with each code. Thrown by [PixplazeWebApi] clients; device flow polling ([PendingAuthorization]) is
/// driven by [OAuthError#AUTHORIZATION_PENDING] and [OAuthError#SLOW_DOWN].
public class OAuthException extends RuntimeException {
    private final OAuthErrorResponse response;

    public OAuthException(OAuthErrorResponse response) {
        super(response.errorDescription() == null
                ? response.error()
                : response.error() + ": " + response.errorDescription());
        this.response = response;
    }

    public OAuthErrorResponse response() {
        return response;
    }

    /// Parsed error code; `null` if the code is unknown to this version of the library.
    public OAuthError error() {
        return response.oauthError();
    }
}
