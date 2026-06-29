package com.pixplaze.api.ext.data.auth;

import com.pixplaze.api.ext.data.player.MinecraftPlayerListInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerPortsInfo;

import java.util.List;

public record MinecraftServerAuthorizationDetails(
        String inviteCode,
        String host,
        String iconBase64,
        MinecraftServerPortsInfo ports,
        List<MinecraftPlayerListInfo> players,
        MinecraftServerInfo minecraftServerInfo
) {
}
