package com.pixplaze.api.ext.data.player;

import java.util.UUID;

public record MinecraftPlayerInfo(
        UUID uuid,
        String username,
        Boolean online,
        Boolean banned,
        Boolean whitelisted,
        Boolean operator,
        Long playtime
) {
    public MinecraftPlayerInfo(UUID uuid, String username) {
        this(uuid, username, null, null, null, null, null);
    }
}
