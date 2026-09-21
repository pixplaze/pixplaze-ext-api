package com.pixplaze.api.ext.data.player;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.UUID;

@RecordBuilder
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
) implements MinecraftPlayerInfoBuilder.With {

    /// Точка входа в билдер прямо с рекорда: {@code MinecraftPlayerInfo.builder()...build()}.
    public static MinecraftPlayerInfoBuilder builder() {
        return MinecraftPlayerInfoBuilder.builder();
    }

    /// Билдер-копия существующего инстанса.
    public static MinecraftPlayerInfoBuilder builder(MinecraftPlayerInfo from) {
        return MinecraftPlayerInfoBuilder.builder(from);
    }

    public MinecraftPlayerInfo(UUID uuid, String username, String ipAddress) {
        this(uuid, username, ipAddress, null, null, null, null, null, null, null);
    }
}
