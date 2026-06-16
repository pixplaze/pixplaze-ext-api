package com.pixplaze.api.ext.data.auth;

public record DeviceResponseInfo(
        String deviceCode,
        String userCode,
        Integer expiresIn,
        Integer interval,
        String verificationUri,
        String verificationUriComplete
) {
}
