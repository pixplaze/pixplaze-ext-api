package com.pixplaze.api.ext.data.server;

import com.pixplaze.api.ext.data.player.MinecraftPlayerInfo;

import java.util.List;

/// Plugin heartbeat body, sent once a minute: only what the plugin is responsible for. No id, name,
/// hosts, motd, core or icon: the id is taken from the token, hosts change only on re-authorization,
/// and motd, core and icon are learned by ping.
///
/// The player snapshot travels by hash. `playersHash` is sent every time; `players` only when the
/// hash differs from the one of the last delivered (2xx) list, and always in the first heartbeat after
/// the plugin start. If Pixplaze has no snapshot or a different hash, it asks for the list in the
/// response ([MinecraftServerHeartbeatResponse#isPlayersRequired]). The snapshot is always complete,
/// so any desync is fixed by the next heartbeat.
///
/// @param isLicense   whether the server checks accounts with Mojang (`online-mode`)
/// @param state       counters and metrics ([MinecraftServerStateInfo#heartbeat])
/// @param playersHash hash of the sorted player snapshot; opaque to Pixplaze, which only compares it
///                    with the hash stored together with the last list
/// @param players     every online player ([MinecraftPlayerInfo#presence]), linked to a profile or not;
///                    `null` — not sent in this heartbeat
public record MinecraftServerHeartbeatInfo(
        Boolean isLicense,
        MinecraftServerStateInfo state,
        String playersHash,
        List<MinecraftPlayerInfo> players
) {

    /// `players` is an immutable defensive copy.
    public MinecraftServerHeartbeatInfo {
        players = players == null ? null : List.copyOf(players);
    }

    /// Heartbeat without the player list: the snapshot is unchanged since the last delivered one.
    public static MinecraftServerHeartbeatInfo of(Boolean isLicense, MinecraftServerStateInfo state, String playersHash) {
        return new MinecraftServerHeartbeatInfo(isLicense, state, playersHash, null);
    }
}
