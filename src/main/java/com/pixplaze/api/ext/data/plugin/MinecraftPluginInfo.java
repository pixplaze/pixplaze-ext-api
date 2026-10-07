package com.pixplaze.api.ext.data.plugin;

/// Plugin installed on a Minecraft server.
///
/// @param name      plugin name from `plugin.yml`
/// @param version   plugin version from `plugin.yml`
/// @param sha256sum SHA-256 of the plugin jar, hex
public record MinecraftPluginInfo(
        String name,
        String version,
        String sha256sum
) {}
