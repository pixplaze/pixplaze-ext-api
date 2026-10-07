package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.auth.AuthorizationDetails;
import com.pixplaze.api.ext.data.auth.AuthorizationToken;

/// Device flow (RFC 8628) of one subject kind: binds its authorization details `D` to the tokens `T`
/// it issues. Obtained from [PixplazeWebApi#server], [PixplazeWebApi#player] or [PixplazeWebApi#operator].
///
/// @param <D> authorization details of the subject
/// @param <T> token pair the flow issues
public interface DeviceAuthorizationClient<D extends AuthorizationDetails, T extends AuthorizationToken> {

    /// Starts the flow: `client_id` is [AuthorizationDetails#clientId], the scope is fixed by the flow.
    /// Show [PendingAuthorization#response] to the user, then wait for the tokens.
    ///
    /// @throws OAuthException if the request is rejected (`invalid_request`, `invalid_scope`, …)
    PendingAuthorization<T> authorize(D details);

    /// Exchanges a refresh token for a new pair (`grant_type=refresh_token`). The old refresh token
    /// becomes invalid immediately; presenting it again revokes the whole chain.
    ///
    /// @throws OAuthException with [com.pixplaze.api.ext.data.oauth.OAuthError#INVALID_GRANT] if the
    ///                        refresh token is unknown, expired or revoked — start the flow over
    T refresh(String refreshToken);
}
