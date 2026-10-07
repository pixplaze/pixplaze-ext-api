package com.pixplaze.api.ext.data.auth;

import java.util.Collection;
import java.util.Optional;

/// Token zone (`aud`) of a Minecraft server: `mc:server:{minecraftServerId}`. Servers are addressed
/// by id rather than by host, so issued tokens survive a change of the server address.
///
/// A plugin accepts an incoming token only if its `aud` contains [#of] its own id.
public final class MinecraftServerTargets {
    private static final String PREFIX = "mc:server:";

    private MinecraftServerTargets() {}

    public static String of(long minecraftServerId) {
        return PREFIX + minecraftServerId;
    }

    /// Server id from the first server zone among `targets`; other zones are skipped.
    public static Optional<Long> minecraftServerIdOf(Collection<String> targets) {
        return targets.stream()
                .filter(target -> target != null && target.startsWith(PREFIX))
                .map(target -> target.substring(PREFIX.length()))
                .flatMap(id -> {
                    try {
                        return Optional.of(Long.parseLong(id)).stream();
                    } catch (NumberFormatException e) {
                        return Optional.<Long>empty().stream();
                    }
                })
                .findFirst();
    }
}
