package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.auth.AuthorizationTokenInfo;
import com.pixplaze.api.ext.data.auth.MinecraftPlayerAuthorizationDetails;
import com.pixplaze.api.ext.data.auth.MinecraftServerAuthorizationDetails;
import com.pixplaze.api.ext.data.auth.VerifiableAuthorizationTokenInfo;

public interface DeviceAuthorization {
    DeviceAuthorizationClient<MinecraftServerAuthorizationDetails, VerifiableAuthorizationTokenInfo> server();

    /// Minecraft player sign-in (scope `MAD:MINECRAFT_PLAYER`).
    DeviceAuthorizationClient<MinecraftPlayerAuthorizationDetails, AuthorizationTokenInfo> player();

    /// Minecraft operator sign-in (scope `MAD:MINECRAFT_OPERATOR`).
    DeviceAuthorizationClient<MinecraftPlayerAuthorizationDetails, AuthorizationTokenInfo> operator();
}
