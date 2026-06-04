package com.pixplaze.api.ext.data.server;

public record MinecraftServerPortsInfo(Integer java, Integer map, Integer api) {
    public MinecraftServerPortsInfo(Integer java) {
        this(java, null, null);
    }
}
