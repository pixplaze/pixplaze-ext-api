package com.pixplaze.api.ext.data.player;

import java.util.List;


/// Represents Minecraft Server player list
///
/// @param max maximum number of online players on the server
/// @param online number of players currently on the server
/// @param list list of online players on the server
/// @param whitelist list of players added to whitelist on the server
/// @param banned list of isBanned players on the server
///
public record MinecraftPlayerListInfo(
        Integer max,
        Integer online,
        List<MinecraftPlayerInfo> list,
        List<MinecraftPlayerInfo> whitelist,
        List<MinecraftPlayerInfo> banned,
        List<MinecraftPlayerInfo> operators
) {
    public MinecraftPlayerListInfo(Integer max, Integer online) {
        this(max, online, null, null, null, null);
    }
}
