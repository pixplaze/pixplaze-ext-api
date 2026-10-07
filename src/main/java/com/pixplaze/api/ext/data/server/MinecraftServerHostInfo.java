package com.pixplaze.api.ext.data.server;

/// Endpoint of a Minecraft server.
///
/// @param minecraftServerId Pixplaze server id; filled by Pixplaze only
/// @param address           domain name or IP address
/// @param port              1..65535
/// @param type              what is served at this endpoint
public record MinecraftServerHostInfo(
        Long minecraftServerId,
        String address,
        Integer port,
        Type type
) {
    public enum Type {
        /// Game address players connect to; unique among all servers.
        HOST,
        /// Web map.
        MAP,
        /// REST API of the Pixplaze plugin.
        API
    }

    /// Game address ([Type#HOST]).
    public static MinecraftServerHostInfo server(String address, Integer port) {
        return new MinecraftServerHostInfo(null, address, port, Type.HOST);
    }
}
