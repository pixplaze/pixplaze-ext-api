package com.pixplaze.api.ext.data.player;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;


/// Represents Minecraft Server player list
///
/// @param max maximum number of online players on the server
/// @param online number of players currently on the server
/// @param list list of online players on the server
/// @param whitelist list of players added to whitelist on the server
/// @param banned list of isBanned players on the server
///
@RecordBuilder
public record MinecraftPlayerListInfo(
        Integer max,
        Integer online,
        List<MinecraftPlayerInfo> list,
        List<MinecraftPlayerInfo> whitelist,
        List<MinecraftPlayerInfo> banned,
        List<MinecraftPlayerInfo> operators
) implements MinecraftPlayerListInfoBuilder.With {

    /// Инварианты контракта в одном месте: списки — иммутабельные защитные копии.
    /// Через этот конструктор проходят и билдер, и {@code with*}-методы.
    public MinecraftPlayerListInfo {
        list = list == null ? null : List.copyOf(list);
        whitelist = whitelist == null ? null : List.copyOf(whitelist);
        banned = banned == null ? null : List.copyOf(banned);
        operators = operators == null ? null : List.copyOf(operators);
    }

    /// Точка входа в билдер прямо с рекорда: {@code MinecraftPlayerListInfo.builder()...build()}.
    public static MinecraftPlayerListInfoBuilder builder() {
        return MinecraftPlayerListInfoBuilder.builder();
    }

    /// Билдер-копия существующего инстанса.
    public static MinecraftPlayerListInfoBuilder builder(MinecraftPlayerListInfo from) {
        return MinecraftPlayerListInfoBuilder.builder(from);
    }

    public MinecraftPlayerListInfo(Integer max, Integer online) {
        this(max, online, null, null, null, null);
    }
}
