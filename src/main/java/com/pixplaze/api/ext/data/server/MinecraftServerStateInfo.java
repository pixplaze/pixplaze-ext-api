package com.pixplaze.api.ext.data.server;

/// Represents the state of the Minecraft server with
/// the most frequently changing data
/// @param online  current number of players online
/// @param tps     server TPS (ticks per second)
/// @param uptime  milliseconds since Minecraft server started
/// @param enabled if server is available on internet
public record MinecraftServerStateInfo(
        Integer online,
        Double tps,
        Long uptime,
        Boolean enabled,
        StateCode state
) {
    enum StateCode {
        MAINTENANCE_ONLINE,
        MAINTENANCE_OFFLINE,
        ONLINE,
        OFFLINE
    }
    public MinecraftServerStateInfo(Boolean enabled) {
        this(null, null, null, enabled, StateCode.OFFLINE);
    }
}
