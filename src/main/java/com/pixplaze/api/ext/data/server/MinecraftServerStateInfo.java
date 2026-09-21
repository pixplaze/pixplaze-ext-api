package com.pixplaze.api.ext.data.server;

import com.pixplaze.api.ext.data.player.MinecraftPlayerListInfo;
import io.soabase.recordbuilder.core.RecordBuilder;

/// Represents the status of the Minecraft server with
/// the most frequently changing data
/// @param tps    server TPS (ticks per second)
/// @param ping   server latency in milliseconds
/// @param uptime milliseconds since Minecraft server started
/// @param status  server current status
@RecordBuilder
public record MinecraftServerStateInfo(
        Double tps,
        Long ping,
        Long uptime,
        String difficulty,
        Status status,
        IntegrationStatus integrationStatus,
        MinecraftPlayerListInfo players
) implements MinecraftServerStateInfoBuilder.With {

    /// Точка входа в билдер прямо с рекорда: {@code MinecraftServerStateInfo.builder()...build()}.
    public static MinecraftServerStateInfoBuilder builder() {
        return MinecraftServerStateInfoBuilder.builder();
    }

    /// Билдер-копия существующего инстанса.
    public static MinecraftServerStateInfoBuilder builder(MinecraftServerStateInfo from) {
        return MinecraftServerStateInfoBuilder.builder(from);
    }

    public enum Status {
        ONLINE,
        OFFLINE,
        BANNED
    }

    public enum IntegrationStatus {
        NATIVE,
        PLUGIN
    }

    public MinecraftServerStateInfo(Status state) {
        this(null, null, null, null, state);
    }

    public MinecraftServerStateInfo(Double tps, Long ping, Long uptime) {
        this(tps, ping, uptime, null, null, IntegrationStatus.NATIVE, null);
    }

    public MinecraftServerStateInfo(
            Double tps,
            Long ping,
            Long uptime,
            String difficulty,
            Status state
    ) {
        this(tps, ping, uptime, difficulty, state, IntegrationStatus.NATIVE, null);
    }
}
