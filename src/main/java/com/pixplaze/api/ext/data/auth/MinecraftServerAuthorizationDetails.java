package com.pixplaze.api.ext.data.auth;

import com.pixplaze.api.ext.data.server.MinecraftServerHostInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;

/// Authorization details of a Minecraft server registering through the device flow
/// (scope `MAD:MINECRAFT_SERVER`).
///
/// @param inviteCode          bid code; required on first registration, absent on re-authorization
/// @param minecraftServerInfo server description; its [MinecraftServerHostInfo.Type#HOST] entry is required
public record MinecraftServerAuthorizationDetails(
        String inviteCode,
        MinecraftServerInfo minecraftServerInfo
) implements AuthorizationDetails {

    /// RFC 8628 `client_id` of the server's flow: `address:port` of the
    /// [MinecraftServerHostInfo.Type#HOST] entry.
    ///
    /// @throws java.util.NoSuchElementException if the server declares no game host
    @Override
    public String clientId() {
        final var host = minecraftServerInfo.host(MinecraftServerHostInfo.Type.HOST).orElseThrow();
        return host.address() + ":" + host.port();
    }
}
