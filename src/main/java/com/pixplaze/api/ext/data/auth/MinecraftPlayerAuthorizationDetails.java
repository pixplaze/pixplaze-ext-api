package com.pixplaze.api.ext.data.auth;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.UUID;

@RecordBuilder
public record MinecraftPlayerAuthorizationDetails(
        Long serverId,
        String host,
        String ipAddress,
        UUID uuid,
        String username,
        String headBase64,
        Boolean isOperator
) implements AuthorizationDetails, MinecraftPlayerAuthorizationDetailsBuilder.With {

    public static MinecraftPlayerAuthorizationDetailsBuilder builder() {
        return MinecraftPlayerAuthorizationDetailsBuilder.builder();
    }

    public static MinecraftPlayerAuthorizationDetailsBuilder builder(MinecraftPlayerAuthorizationDetails from) {
        return MinecraftPlayerAuthorizationDetailsBuilder.builder(from);
    }
}
