package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.server.MinecraftServerHeartbeatInfo;
import com.pixplaze.api.ext.data.server.MinecraftServerHeartbeatResponse;

/// Pixplaze API client of a Minecraft server.
///
/// Each device flow is a [DeviceAuthorizationClient] typed by its authorization details and tokens:
///
/// ```java
/// var pending = api.server().authorize(details);
/// console.info("Code: " + pending.response().userCode());
/// pending.await().thenAccept(tokens -> store(tokens.refreshToken(), tokens.publicKey()));
/// ```
///
/// Token responses are snake_case on the wire (`access_token`, `refresh_token`, `public_key`), plus
/// `token_type` and `expires_in`, which the token types do not carry: the token lifetime is the `exp`
/// claim of the access token.
///
/// Every call throws [OAuthException] on an OAuth error response.
public interface PixplazeWebApi {

    /// Minecraft server registration and re-authorization (scope `MAD:MINECRAFT_SERVER`). The public
    /// signing key comes with every token pair, refresh included.
    DeviceAuthorization authorization();

    /// Sends the plugin heartbeat. If the response asks for players
    /// ([MinecraftServerHeartbeatResponse#isPlayersRequired]), send it again at once with the player list.
    ///
    /// @param accessToken server access token
    MinecraftServerHeartbeatResponse heartbeat(String accessToken, MinecraftServerHeartbeatInfo heartbeat);
}
