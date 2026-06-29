package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.Authority;
import com.pixplaze.api.ext.data.auth.AuthorizationToken;
import com.pixplaze.api.ext.data.auth.AuthorizationTokenInfo;
import com.pixplaze.api.ext.data.auth.MinecraftServerAuthorizationDetails;
import com.pixplaze.api.ext.data.auth.VerifiableAuthorizationTokenInfo;
import com.pixplaze.api.ext.data.player.MinecraftPlayerInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerInfo;

import static com.pixplaze.api.ext.data.Authority.Role.*;
import static com.pixplaze.api.ext.data.Authority.Source.MINECRAFT_AUTHORIZED_DEVICE;

public interface PixplazeApi {
    /// Authenticates clientId by
    /// @param clientId RFC 8628 `client_id`
    /// @param authority RFC 8628 `scope`
    <D, T extends AuthorizationToken> T authorize(String clientId, Authority authority, D details);

    <T extends AuthorizationToken> T token(String clientId, String deviceCode);

    /// Authorizes minecraft server as application
    /// @param authorizationDetails authorization details for Minecraft server.
    /// the only required parameter is [MinecraftServerInfo#host]
    /// and it must not be null
    default VerifiableAuthorizationTokenInfo authorizeMinecraftServer(MinecraftServerAuthorizationDetails authorizationDetails) {
        return authorize(authorizationDetails.minecraftServerInfo().host(), Authority.as(MINECRAFT_SERVER).from(MINECRAFT_AUTHORIZED_DEVICE).unauthorized(), authorizationDetails);
    }

    /// Authorizes minecraft player as regular player
    /// @param authorizationDetails authorization details for Minecraft player
    /// the only required parameters are [MinecraftPlayerInfo#uuid], [MinecraftPlayerInfo#username]
    /// and they must not be null
    default AuthorizationTokenInfo authorizeMinecraftPlayer(MinecraftPlayerInfo authorizationDetails) {
        return authorize(authorizationDetails.uuid().toString(), Authority.as(MINECRAFT_PLAYER).from(MINECRAFT_AUTHORIZED_DEVICE).unauthorized(), authorizationDetails);
    }

    /// Authorizes minecraft player as regular player
    /// @param authorizationDetails authorization details for Minecraft isOperator
    /// the only required parameters are [MinecraftPlayerInfo#uuid], [MinecraftPlayerInfo#username], [MinecraftPlayerInfo#isOperator()]
    /// and they must not be null
    default AuthorizationTokenInfo authorizeMinecraftOperator(MinecraftPlayerInfo authorizationDetails) {
        return authorize(authorizationDetails.uuid().toString(), Authority.as(MINECRAFT_OPERATOR).from(MINECRAFT_AUTHORIZED_DEVICE).unauthorized(), authorizationDetails);
    }
}
