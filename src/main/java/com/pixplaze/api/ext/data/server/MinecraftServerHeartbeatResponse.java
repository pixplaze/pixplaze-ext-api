package com.pixplaze.api.ext.data.server;

/// Result of a plugin heartbeat ([MinecraftServerHeartbeatInfo]).
///
/// @param isPlayersRequired `true` — Pixplaze has no player snapshot of the server or its hash differs
///                          (for example, the presence store lost its data): send the heartbeat again
///                          at once, with `players`. The plugin cannot detect this itself, its own hash
///                          did not change.
public record MinecraftServerHeartbeatResponse(Boolean isPlayersRequired) {
}
