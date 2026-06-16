package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.Authority;
import com.pixplaze.api.ext.data.auth.AuthorizationTokenInfo;

import static com.pixplaze.api.ext.data.Authority.Role.*;
import static com.pixplaze.api.ext.data.Authority.Source.MINECRAFT_AUTHORIZED_DEVICE;

public interface PixplazeApi {
    /// Authenticates client by
    /// @param client RFC 8628 `client_id`
    /// @param role RFC 8628 `scope`
    AuthorizationTokenInfo authenticate(String client, Authority role);

    default AuthorizationTokenInfo authenticateMinecraftServer(String hostname) {
        return authenticate(hostname, Authority.as(USER).from(MINECRAFT_AUTHORIZED_DEVICE).withoutPermissions());
    }

    default AuthorizationTokenInfo authenticateMinecraftPlayer(String username) {
        return authenticate(username, Authority.as(APPLICATION).from(MINECRAFT_AUTHORIZED_DEVICE).withoutPermissions());
    }

    default AuthorizationTokenInfo authenticateMinecraftOperator(String username) {
        return authenticate(username, Authority.as(ADMINISTRATOR).from(MINECRAFT_AUTHORIZED_DEVICE).withoutPermissions());
    }
}
