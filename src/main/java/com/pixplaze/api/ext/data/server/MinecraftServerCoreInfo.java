package com.pixplaze.api.ext.data.server;

/// Represents minecraft server core such as Bukkit, Spigot, Paper, etc.
/// @param name    core name e.g. Paper
/// @param version core version
public record MinecraftServerCoreInfo(
        String name,
        String version
) {}
