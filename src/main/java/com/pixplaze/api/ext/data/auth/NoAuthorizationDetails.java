package com.pixplaze.api.ext.data.auth;

/// Empty authorization details: the subject is identified by the approving user alone
/// (application sign-in, scope `AAD:USER`).
public record NoAuthorizationDetails() implements AuthorizationDetails {

    @Override
    public String clientId() {
        return null;
    }
}
