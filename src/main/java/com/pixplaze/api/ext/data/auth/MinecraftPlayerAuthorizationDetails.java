package com.pixplaze.api.ext.data.auth;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.UUID;

/// Authorization details of a Minecraft player signing in through the device flow
/// (scopes `MAD:MINECRAFT_PLAYER` and `MAD:MINECRAFT_OPERATOR`).
///
/// @param minecraftServerId id of the server the player plays on; required, the server must exist and not be banned
/// @param ipAddress         player's IP address; required
/// @param uuid              required; also the `client_id` of the flow, see [#clientId]
/// @param username          required
/// @param skinHeadBase64    skin head image, shown to the approving user
/// @param isOperator        whether the player is an operator on this server
@RecordBuilder
public record MinecraftPlayerAuthorizationDetails(
        Long minecraftServerId,
        String ipAddress,
        UUID uuid,
        String username,
        String skinHeadBase64,
        Boolean isOperator
) implements AuthorizationDetails, MinecraftPlayerAuthorizationDetailsBuilder.With {

    public static MinecraftPlayerAuthorizationDetailsBuilder builder() {
        return MinecraftPlayerAuthorizationDetailsBuilder.builder();
    }

    public static MinecraftPlayerAuthorizationDetailsBuilder builder(MinecraftPlayerAuthorizationDetails from) {
        return MinecraftPlayerAuthorizationDetailsBuilder.builder(from);
    }

    /// RFC 8628 `client_id` of the player's flow: the player UUID.
    @Override
    public String clientId() {
        return uuid.toString();
    }
}
