package com.pixplaze.api.ext.data.server;

/// Represents a Minecraft server present in pixplaze system
/// @param host
/// @param port
/// @param server
public record PixplazeServerInfo(
        String host,
        Integer port,
        MinecraftServerInfo server
) {}
