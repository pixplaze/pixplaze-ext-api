package com.pixplaze.api.ext.data.server;

import com.pixplaze.api.ext.data.plugin.MinecraftPluginInfo;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;
import java.util.Optional;

/// Minecraft server of any core.
///
/// The description changes rarely; what changes from sample to sample lives in [#state]. Every
/// description field has a single source: `motd`, `iconBase64` and `core` are learned by pinging
/// the server over the Minecraft protocol, `isLicense` comes from the plugin heartbeat
/// ([#heartbeat]), `hosts` only from registration and re-authorization.
///
/// Views: [#preview], [#connection], [#heartbeat].
///
/// @param id          Pixplaze server id
/// @param name        display name chosen on registration
/// @param motd        message of the day as shown in the Minecraft server list
/// @param isLicense   whether the server checks accounts with Mojang (`online-mode`)
/// @param iconBase64  server icon, Base64 PNG
/// @param description long description written by the owner
/// @param hosts       server endpoints, at most one per [MinecraftServerHostInfo.Type]
/// @param core        server core, e.g. Paper 1.21.4
/// @param state       frequently changing state
/// @param plugins     installed plugins
/// @param rating      average user rating
/// @param ratingCount number of user ratings
@RecordBuilder
public record MinecraftServerInfo(
        Long id,
        String name,
        String motd,
        Boolean isLicense,
        String iconBase64,
        String description,
        List<MinecraftServerHostInfo> hosts,
        MinecraftServerCoreInfo core,
        MinecraftServerStateInfo state,
        List<MinecraftPluginInfo> plugins,
        Double rating,
        Long ratingCount
) implements MinecraftServerInfoBuilder.With {

    /// `hosts` and `plugins` are immutable defensive copies. The builder and the `with*` methods
    /// go through this constructor too.
    public MinecraftServerInfo {
        hosts = hosts == null ? null : List.copyOf(hosts);
        plugins = plugins == null ? null : List.copyOf(plugins);
    }

    /// Builder entry point: `MinecraftServerInfo.builder()...build()`.
    public static MinecraftServerInfoBuilder builder() {
        return MinecraftServerInfoBuilder.builder();
    }

    /// Builder pre-filled with a copy of `from`: `MinecraftServerInfo.builder(existing).name(x).build()`.
    public static MinecraftServerInfoBuilder builder(MinecraftServerInfo from) {
        return MinecraftServerInfoBuilder.builder(from);
    }

    /// Preview of a server being registered: name, game address and icon.
    public static MinecraftServerInfo preview(String name, String address, Integer port, String iconBase64) {
        return builder()
                .name(name)
                .hosts(List.of(MinecraftServerHostInfo.server(address, port)))
                .iconBase64(iconBase64).build();
    }

    /// Game address only: what is needed to connect to or ping the server.
    public static MinecraftServerInfo connection(String address, Integer port) {
        return builder().hosts(List.of(MinecraftServerHostInfo.server(address, port))).build();
    }

    /// Plugin heartbeat body: only what the plugin is responsible for — the license flag and the
    /// state ([MinecraftServerStateInfo#heartbeat]). No id, name, hosts, motd, core or icon: the id
    /// is taken from the token, hosts change only on re-authorization, and motd, core and icon are
    /// learned by ping.
    public static MinecraftServerInfo heartbeat(Boolean isLicense, MinecraftServerStateInfo state) {
        return builder()
                .isLicense(isLicense)
                .state(state)
                .build();
    }

    /// Endpoint of the given type, if the server declared one.
    public Optional<MinecraftServerHostInfo> host(MinecraftServerHostInfo.Type type) {
        return hosts == null
                ? Optional.empty()
                : hosts.stream().filter(host -> host.type() == type).findFirst();
    }
}
