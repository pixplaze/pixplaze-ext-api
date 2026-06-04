package com.pixplaze.api.ext.data.server;

import java.util.List;
import java.util.Map;

/// Represents plain Minecraft server of any core
///
/// @param host server host
/// @param motd simple server description
/// @param favicon Base64 server image string
public record MinecraftServerInfo(
        String host,
        String motd,
        Boolean license,
        String favicon,
        MinecraftServerPortsInfo ports,
        MinecraftServerCoreInfo core,
        MinecraftServerStateInfo state,
        List<String> plugins,
        Map<String, Object> metadata
) {
    public MinecraftServerInfo(String host, MinecraftServerPortsInfo ports) {
        this(host, ports, null);
    }

    public MinecraftServerInfo(String host, MinecraftServerPortsInfo ports, MinecraftServerStateInfo minecraftServerStateInfo) {
        this(
                host,
                null,
                null,
                null,
                ports,
                null,
                minecraftServerStateInfo,
                null,
                null
        );
    }
}
