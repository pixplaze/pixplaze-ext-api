package com.pixplaze.api.ext.data.server;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

/// Represents plain Minecraft server of any core
///
/// @param host server host
/// @param motd simple server description
/// @param iconBase64 Base64 server image string
@RecordBuilder
public record MinecraftServerInfo(
        Long id,
        String name,
        String host,
        String motd,
        Boolean license,
        String iconBase64,
        String description,
        MinecraftServerPortsInfo ports,
        MinecraftServerCoreInfo core,
        MinecraftServerStateInfo state,
        List<String> plugins,
        Double rating,
        Long ratingCount
) implements MinecraftServerInfoBuilder.With {

    /// Инварианты контракта в одном месте: {@code plugins} — иммутабельная защитная копия.
    /// Через этот конструктор проходят и билдер, и {@code with*}-методы.
    public MinecraftServerInfo {
        plugins = plugins == null ? null : List.copyOf(plugins);
    }

    /// Точка входа в билдер прямо с рекорда: {@code MinecraftServerInfo.builder()...build()}.
    public static MinecraftServerInfoBuilder builder() {
        return MinecraftServerInfoBuilder.builder();
    }

    /// Билдер-копия существующего инстанса: {@code MinecraftServerInfo.builder(existing).host(x).build()}.
    public static MinecraftServerInfoBuilder builder(MinecraftServerInfo from) {
        return MinecraftServerInfoBuilder.builder(from);
    }

    public static MinecraftServerInfo preview(String name, String host, Integer port, String icon) {
        return builder()
                .name(name)
                .host(host)
                .ports(new MinecraftServerPortsInfo(port))
                .iconBase64(icon).build();
    }

    public static MinecraftServerInfo connection(String host, Integer port) {
        return builder().host(host).ports(new MinecraftServerPortsInfo(port)).build();
    }
}
