package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.Authority;
import com.pixplaze.api.ext.data.auth.AuthorizationTokenInfo;
import com.pixplaze.api.ext.data.player.MinecraftPlayerInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;

import static com.pixplaze.api.ext.data.Authority.Role.*;
import static com.pixplaze.api.ext.data.Authority.Source.MINECRAFT_AUTHORIZED_DEVICE;

public interface PixplazeApi {
    /// Authenticates clientId by
    /// @param clientId RFC 8628 `client_id`
    /// @param authority RFC 8628 `scope`
    <D> AuthorizationTokenInfo authorize(String clientId, Authority authority, D details);

    /// Authorizes minecraft server as application
    /// @param minecraftServerInfo authorization details for Minecraft server.
    /// the only required parameter is [MinecraftServerInfo#host]
    /// and it must not be null
    default AuthorizationTokenInfo authorizeMinecraftServer(MinecraftServerInfo minecraftServerInfo) {
        return authorize(minecraftServerInfo.host(), Authority.as(APPLICATION).from(MINECRAFT_AUTHORIZED_DEVICE).unauthorized(), minecraftServerInfo);
    }

    /// Authorizes minecraft player as regular player
    /// @param minecraftPlayerInfo authorization details for Minecraft player
    /// the only required parameters are [MinecraftPlayerInfo#uuid], [MinecraftPlayerInfo#username]
    /// and they must not be null
    default AuthorizationTokenInfo authorizeMinecraftPlayer(MinecraftPlayerInfo minecraftPlayerInfo) {
        return authorize(minecraftPlayerInfo.uuid().toString(), Authority.as(USER).from(MINECRAFT_AUTHORIZED_DEVICE).unauthorized(), minecraftPlayerInfo);
    }

    /// Authorizes minecraft player as regular player
    /// @param minecraftPlayerInfo authorization details for Minecraft operator
    /// the only required parameters are [MinecraftPlayerInfo#uuid], [MinecraftPlayerInfo#username], [MinecraftPlayerInfo#operator()]
    /// and they must not be null
    default AuthorizationTokenInfo authorizeMinecraftOperator(MinecraftPlayerInfo minecraftPlayerInfo) {
        return authorize(minecraftPlayerInfo.uuid().toString(), Authority.as(ADMINISTRATOR).from(MINECRAFT_AUTHORIZED_DEVICE).unauthorized(), minecraftPlayerInfo);
    }
}
