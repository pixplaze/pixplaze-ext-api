package com.pixplaze.api.ext.data.player;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

/// Players of a Minecraft server.
///
/// @param max       maximum number of players online
/// @param online    number of players online now
/// @param list      players online now
/// @param whitelist players on the whitelist
/// @param banned    banned players
/// @param operators server operators
@RecordBuilder
public record MinecraftPlayerListInfo(
        Integer max,
        Integer online,
        List<MinecraftPlayerInfo> list,
        List<MinecraftPlayerInfo> whitelist,
        List<MinecraftPlayerInfo> banned,
        List<MinecraftPlayerInfo> operators
) implements MinecraftPlayerListInfoBuilder.With {

    /// The lists are immutable defensive copies. The builder and the `with*` methods go through
    /// this constructor too.
    public MinecraftPlayerListInfo {
        list = list == null ? null : List.copyOf(list);
        whitelist = whitelist == null ? null : List.copyOf(whitelist);
        banned = banned == null ? null : List.copyOf(banned);
        operators = operators == null ? null : List.copyOf(operators);
    }

    /// Builder entry point: `MinecraftPlayerListInfo.builder()...build()`.
    public static MinecraftPlayerListInfoBuilder builder() {
        return MinecraftPlayerListInfoBuilder.builder();
    }

    /// Builder pre-filled with a copy of `from`.
    public static MinecraftPlayerListInfoBuilder builder(MinecraftPlayerListInfo from) {
        return MinecraftPlayerListInfoBuilder.builder(from);
    }

    /// Counters only, no player lists.
    public MinecraftPlayerListInfo(Integer max, Integer online) {
        this(max, online, null, null, null, null);
    }
}
