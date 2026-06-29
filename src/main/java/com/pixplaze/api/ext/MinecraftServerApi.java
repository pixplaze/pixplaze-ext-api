package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.player.MinecraftPlayerInfo;
import com.pixplaze.api.ext.data.plugin.MinecraftPluginInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerStateInfo;

import java.util.List;
import java.util.List;

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

    List<MinecraftPlayerInfo> getOnlinePlayers();
    List<MinecraftPlayerInfo> getOfflinePlayers();
    List<MinecraftPlayerInfo> getBannedPlayers();
    List<MinecraftPlayerInfo> getWhitelistedPlayers();
    List<MinecraftPlayerInfo> getOpPlayers();
    List<MinecraftPlayerInfo> getAllPlayers();
}
