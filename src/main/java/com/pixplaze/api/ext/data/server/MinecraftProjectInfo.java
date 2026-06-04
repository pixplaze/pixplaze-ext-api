package com.pixplaze.api.ext.data.server;

import java.util.List;

/// Represents a Minecraft server present in pixplaze system
/// @param host
/// @param port
/// @param servers
/// @deprecated use {@link MinecraftServerInfo} instead
@Deprecated(forRemoval = true)
public record MinecraftProjectInfo(
        String host,
        Integer port,
        Double rating,
        List<MinecraftServerInfo> servers
) {}
