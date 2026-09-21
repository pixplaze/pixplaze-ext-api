package com.pixplaze.api.ext.data.auth;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record DeviceResponseInfo(
        String deviceCode,
        String userCode,
        Integer expiresIn,
        Integer interval,
        String verificationUri,
        String verificationUriComplete
) implements DeviceResponseInfoBuilder.With {

    /// Точка входа в билдер прямо с рекорда: {@code DeviceResponseInfo.builder()...build()}.
    public static DeviceResponseInfoBuilder builder() {
        return DeviceResponseInfoBuilder.builder();
    }

    /// Билдер-копия существующего инстанса.
    public static DeviceResponseInfoBuilder builder(DeviceResponseInfo from) {
        return DeviceResponseInfoBuilder.builder(from);
    }
}
