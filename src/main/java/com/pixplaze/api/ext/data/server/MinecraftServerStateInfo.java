package com.pixplaze.api.ext.data.server;

import com.pixplaze.api.ext.data.player.MinecraftPlayerListInfo;

/// Represents the state of the Minecraft server with
/// the most frequently changing data
/// @param tps    server TPS (ticks per second)
/// @param ping   server latency in milliseconds
/// @param uptime milliseconds since Minecraft server started
/// @param state  server current state
public record MinecraftServerStateInfo(
        Double tps,
        Long ping,
        Long uptime,
        String difficulty,
        StateCode state,
        MinecraftPlayerListInfo players
) {
    public enum StateCode {
        MAINTENANCE,
        ONLINE,
        OFFLINE
    }

    public MinecraftServerStateInfo(StateCode state) {
        this(null, null, null, null, state);
    }

    public MinecraftServerStateInfo(Double tps, Long ping, Long uptime) {
        this(tps, ping, uptime, null, null, null);
    }

    public MinecraftServerStateInfo(
            Double tps,
            Long ping,
            Long uptime,
            String difficulty,
            StateCode state
    ) {
        this(tps, ping, uptime, difficulty, state, null);
    }
}
