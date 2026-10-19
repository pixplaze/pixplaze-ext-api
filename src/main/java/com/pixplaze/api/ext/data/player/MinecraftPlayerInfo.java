package com.pixplaze.api.ext.data.player;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.UUID;

/// Minecraft player as seen by a server.
///
/// @param uuid           player UUID
/// @param username       nickname
/// @param ipAddress      last known IP address
/// @param isOnline       whether the player is on the server now
/// @param isBanned       whether the player is banned on the server
/// @param isWhitelisted  whether the player is on the server whitelist
/// @param isOperator     whether the player is a server operator
/// @param playtime       time played on the server, milliseconds
/// @param skinBase64     skin texture, Base64 PNG
/// @param skinHeadBase64 skin head image, Base64 PNG
/// @param isHidden       whether the player hides from others on the server (vanish): friends do
///                       not see such a player online
///
/// Views: [#presence].
@RecordBuilder
public record MinecraftPlayerInfo(
        UUID uuid,
        String username,
        String ipAddress,
        Boolean isOnline,
        Boolean isBanned,
        Boolean isWhitelisted,
        Boolean isOperator,
        Long playtime,
        String skinBase64,
        String skinHeadBase64,
        Boolean isHidden
) implements MinecraftPlayerInfoBuilder.With {

    /// Builder entry point: `MinecraftPlayerInfo.builder()...build()`.
    public static MinecraftPlayerInfoBuilder builder() {
        return MinecraftPlayerInfoBuilder.builder();
    }

    /// Builder pre-filled with a copy of `from`.
    public static MinecraftPlayerInfoBuilder builder(MinecraftPlayerInfo from) {
        return MinecraftPlayerInfoBuilder.builder(from);
    }

    public MinecraftPlayerInfo(UUID uuid, String username, String ipAddress) {
        this(uuid, username, ipAddress, null, null, null, null, null, null, null, null);
    }

    /// Player in the heartbeat snapshot ([com.pixplaze.api.ext.data.server.MinecraftServerHeartbeatInfo]):
    /// an online player with the nickname, operator and vanish flags only.
    public static MinecraftPlayerInfo presence(UUID uuid, String username, Boolean isOperator, Boolean isHidden) {
        return builder()
                .uuid(uuid)
                .username(username)
                .isOnline(true)
                .isOperator(isOperator)
                .isHidden(isHidden)
                .build();
    }
}
