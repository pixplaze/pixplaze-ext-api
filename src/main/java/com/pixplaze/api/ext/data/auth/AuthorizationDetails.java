package com.pixplaze.api.ext.data.auth;

/// Subject-specific part of a device authorization request (`authorization_details`):
/// what is being authorized. Its shape is defined by the requested scope.
public interface AuthorizationDetails {

    /// RFC 8628 `client_id` the flow is started and polled with; derived from the details.
    /// `null` for subjects without their own identity ([NoAuthorizationDetails]).
    String clientId();
}
