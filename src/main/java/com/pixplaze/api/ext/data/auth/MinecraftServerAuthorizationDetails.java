package com.pixplaze.api.ext.data.auth;

import com.pixplaze.api.ext.data.server.MinecraftServerInfo;

public record MinecraftServerAuthorizationDetails(
        String inviteCode,
        MinecraftServerInfo minecraftServerInfo
) implements AuthorizationDetails {
}
