package com.pixplaze.api.ext.data.auth;

import java.util.UUID;

public record MinecraftPlayerAuthorizationDetails(
        Long serverId,
        String host,
        String ipAddress,
        UUID uuid,
        String username,
        String headBase64,
        Boolean isOperator
) {
}
