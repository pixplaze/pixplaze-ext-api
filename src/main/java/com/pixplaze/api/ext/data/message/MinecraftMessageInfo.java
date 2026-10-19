package com.pixplaze.api.ext.data.message;

import java.util.UUID;

/// Message of a Minecraft server: a console line or a chat message. Console and chat streams use the
/// same record and differ in [#type()].
///
/// The text is raw: it may hold Minecraft formatting codes (`§`) and ANSI escape sequences. The
/// author is a separate field, so the receiver decides how to present it (`<Steve> hi`, `[Server] hi`,
/// `* Steve waves`). [Type#CHAT] is the exception: it keeps the server formatting, see the parameters.
///
/// @param type       kind of the message
/// @param author     author name: player name, command sender name or, for [Type#CONSOLE], the logger
///                   name; `null` — the server itself. For [Type#CHAT] — the player display name as
///                   the server shows it, with prefixes and formatting codes
/// @param authorUuid UUID of the player who is the author; `null` — the author is not a player
/// @param text       message text without the author. For [Type#CHAT] — the whole chat line as players
///                   see it (author included, with formatting codes), or only the message text if the
///                   server chat format could not be applied
/// @param timestamp  when the message was produced, milliseconds since the epoch (UTC)
public record MinecraftMessageInfo(
        Type type,
        String author,
        UUID authorUuid,
        String text,
        Long timestamp
) {

    public enum Type {
        /// Line of the server log.
        CONSOLE,
        /// Player chat message.
        CHAT,
        /// Player joined; the text is the join message players see.
        JOIN,
        /// Player left; the text is the quit message players see.
        QUIT,
        /// Player died; the text is the death message players see.
        DEATH,
        /// Server-wide broadcast by a plugin.
        BROADCAST,
        /// `/say` — announcement by the author.
        SAY,
        /// `/me` — action of the author.
        ME,
        /// Message sent from the Pixplaze web chat; the author is the player the web user acts as.
        WEB,
        /// Reply to a command of one web client, delivered to that client only.
        REPLY
    }
}
