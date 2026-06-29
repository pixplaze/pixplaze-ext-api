package com.pixplaze.api.ext.data.player;

import java.util.UUID;

public record MinecraftPlayerInfo(
        UUID uuid,
        String username,
        String ipAddress,
        Boolean online,
        Boolean isBanned,
        Boolean isWhitelisted,
        Boolean isOperator,
        Long playtime,
        String skinBase64,
        String skinHeadBase64
) {
    public MinecraftPlayerInfo(UUID uuid, String username, String ipAddress) {
        this(uuid, username, ipAddress, null, null, null, null, null, null, null);
    }
}
