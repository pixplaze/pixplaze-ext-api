package com.pixplaze.api.ext.data.auth;

import com.pixplaze.api.ext.data.server.MinecraftServerHostInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;

/// Authorization details of a Minecraft server registering through the device flow
/// (scope `MAD:MINECRAFT_SERVER`).
///
/// @param inviteCode          bid code; required on first registration, absent on re-authorization
/// @param minecraftServerInfo server description; on registration the game address ([MinecraftServerHostInfo.Type#HOST])
///                            is taken from the bid, entries without an address get the bid's address
public record MinecraftServerAuthorizationDetails(
        String inviteCode,
        MinecraftServerInfo minecraftServerInfo
) implements AuthorizationDetails {

    /// RFC 8628 `client_id` of the server's flow: the same value must be used to start and to poll.
    /// `address:port` of the [MinecraftServerHostInfo.Type#HOST] entry if the server knows it; otherwise
    /// `server:<id>` on re-authorization or `bid:<inviteCode>` on registration — on registration Pixplaze
    /// takes the game address from the bid anyway.
    @Override
    public String clientId() {
        final var host = minecraftServerInfo == null ? null : minecraftServerInfo.host(MinecraftServerHostInfo.Type.HOST).orElse(null);
        if (host != null) {
            return host.address() + ":" + host.port();
        }
        if (minecraftServerInfo != null && minecraftServerInfo.id() != null) {
            return "server:" + minecraftServerInfo.id();
        }
        return "bid:" + inviteCode;
    }
}
