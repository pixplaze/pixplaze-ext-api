package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.player.MinecraftPlayerInfo;
import com.pixplaze.api.ext.data.plugin.MinecraftPluginInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerStateInfo;

import java.util.List;
import java.util.Set;

/// Represents Minecraft server side API
@SuppressWarnings("unused")
public interface MinecraftServerApi {

    default MinecraftServerInfo getServerInfo() {
        return getServerInfo(true);
    }
    MinecraftServerInfo getServerInfo(boolean fetchThumbnail);
    MinecraftServerStateInfo getServerState();

    List<MinecraftPluginInfo> getInstalledPlugins();
    List<MinecraftPluginInfo> getEnabledPlugins();

    Set<MinecraftPlayerInfo> getOnlinePlayers();
    Set<MinecraftPlayerInfo> getOfflinePlayers();
    Set<MinecraftPlayerInfo> getBannedPlayers();
    Set<MinecraftPlayerInfo> getWhitelistedPlayers();
    Set<MinecraftPlayerInfo> getOpPlayers();
    Set<MinecraftPlayerInfo> getAllPlayers();
}
