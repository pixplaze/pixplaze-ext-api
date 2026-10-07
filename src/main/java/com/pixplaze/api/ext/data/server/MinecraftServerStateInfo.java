package com.pixplaze.api.ext.data.server;

import com.pixplaze.api.ext.data.player.MinecraftPlayerListInfo;
import io.soabase.recordbuilder.core.RecordBuilder;

/// State of a Minecraft server: only what changes from sample to sample. The description
/// (motd, icon, core, plugins, license) changes rarely and lives in [MinecraftServerInfo].
///
/// Pixplaze assembles the state from two parts: a ping over the Minecraft protocol (any server)
/// and the plugin heartbeat (only [IntegrationStatus#PLUGIN], see [#heartbeat]). While the
/// heartbeat is fresh (not older than 2 minutes), its values win in the overlapping fields:
/// `ping`, `players` and `status`.
///
/// Views: [#heartbeat].
///
/// @param minecraftServerId Pixplaze server id; filled by Pixplaze only, ignored in an incoming
///                          heartbeat — the server is identified by its token
/// @param tps               ticks per second; plugin only
/// @param ping              milliseconds: for a server with a plugin and a fresh heartbeat — the median
///                          ping of its players (what they actually get), otherwise — the latency from
///                          Pixplaze to the server over the Minecraft protocol
/// @param uptime            milliseconds since the server start; plugin only
/// @param difficulty        game difficulty; plugin only
/// @param status            ONLINE or OFFLINE by the latest observation; BANNED is set by Pixplaze
/// @param integrationStatus NATIVE — server without the plugin, PLUGIN — with the plugin
/// @param players           players; in a heartbeat and in Pixplaze responses — counters only
///                          (`max`, `online`)
@RecordBuilder
public record MinecraftServerStateInfo(
        Long minecraftServerId,
        Double tps,
        Long ping,
        Long uptime,
        String difficulty,
        Status status,
        IntegrationStatus integrationStatus,
        MinecraftPlayerListInfo players
) implements MinecraftServerStateInfoBuilder.With {

    /// Builder entry point: `MinecraftServerStateInfo.builder()...build()`.
    public static MinecraftServerStateInfoBuilder builder() {
        return MinecraftServerStateInfoBuilder.builder();
    }

    /// Builder pre-filled with a copy of `from`.
    public static MinecraftServerStateInfoBuilder builder(MinecraftServerStateInfo from) {
        return MinecraftServerStateInfoBuilder.builder(from);
    }

    /// State sent by the plugin heartbeat: no server id and no player lists, counters only.
    /// The plugin is running, so the server is ONLINE with the PLUGIN integration.
    ///
    /// @param ping median ping of the players, milliseconds
    public static MinecraftServerStateInfo heartbeat(
            Double tps,
            Long ping,
            Long uptime,
            String difficulty,
            Integer playersOnline,
            Integer playersMax
    ) {
        return builder()
                .tps(tps)
                .ping(ping)
                .uptime(uptime)
                .difficulty(difficulty)
                .status(Status.ONLINE)
                .integrationStatus(IntegrationStatus.PLUGIN)
                .players(new MinecraftPlayerListInfo(playersMax, playersOnline))
                .build();
    }

    public enum Status {
        ONLINE,
        OFFLINE,
        BANNED
    }

    public enum IntegrationStatus {
        NATIVE,
        PLUGIN
    }
}
