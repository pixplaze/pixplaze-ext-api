package com.pixplaze.api.ext.data;

public enum DeviceAuthenticatedAuthority {
    MINECRAFT_AUTHORIZED_DEVICE, // Разрешает действовать от имени пользователя в Minecraft
    APPLICATION_AUTHORIZED_DEVICE, // Разрешает действовать от имени пользователя в приложении
    NOT_AUTHORIZED_DEVICE // Не разрешает доступ к профилю
}
