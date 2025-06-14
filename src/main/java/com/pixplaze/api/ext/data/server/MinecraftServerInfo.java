package com.pixplaze.api.ext.data.server;

import java.util.List;

/// Represents plain Minecraft server of any core
/// @param address    server address
/// @param name       simple server name
/// @param thumbnail  Base64 server image string
/// @param version    minecraft server version
/// @param maxPlayers maximum players on server
/// @param difficulty server difficulty
public record MinecraftServerInfo(
        String address,
        Integer port,
        String name,
        Boolean license,
        String difficulty,
        Boolean hardcore,
        Boolean whitelist,
        String version,
        Integer maxPlayers,
        String thumbnail,
        MinecraftServerCoreInfo core,
        MinecraftServerStateInfo state,
        List<String> plugins
) {}
