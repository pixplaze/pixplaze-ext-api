package com.pixplaze.api.ext.data.plugin;

public record MinecraftPluginInfo(
        String name,
        String version,
        String sha256sum
) {}
