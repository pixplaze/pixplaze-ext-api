package com.pixplaze.api.ext.data.auth;

/// Fine-grained permission codes shared by web-api, web-app and the plugin.
///
/// Format: `<domain>.<resource>.<action>`, `*` replaces whole segments (`sys.*`, `sys.profile.*`).
/// - `sys` — operations served by web-api, including operations on a server record
///   (`sys.servers.delete`): the server is taken from the request path.
/// - `mc` — operations served by the plugin of a server; the server is the token's audience
///   (`mc:server:{id}`), it never appears in the code.
///
/// Which role grants which permission is defined by the `role_permission` table of web-api, not here.
public final class Permissions {

    /// Every web-api operation.
    public static final String SYS_ALL = "sys.*";
    /// Own profile: read and edit.
    public static final String SYS_PROFILE_ALL = "sys.profile.*";
    /// Public server list.
    public static final String SYS_SERVERS_LIST_READ = "sys.servers.list.read";
    /// Server rating: vote and withdraw a vote.
    public static final String SYS_SERVERS_RATING_ALL = "sys.servers.rating.*";
    /// Favorite servers: add and remove.
    public static final String SYS_SERVERS_FAVORITES_ALL = "sys.servers.favorites.*";
    /// Approve server bids.
    public static final String SYS_SERVERS_BIDS_APPROVE = "sys.servers.bids.approve";
    /// Remove a server from Pixplaze.
    public static final String SYS_SERVERS_DELETE = "sys.servers.delete";

    /// Read the server chat.
    public static final String MC_CHAT_READ = "mc.chat.read";
    /// Write to the server chat.
    public static final String MC_CHAT_WRITE = "mc.chat.write";
    /// Read the server console.
    public static final String MC_CONSOLE_READ = "mc.console.read";
    /// Run console commands.
    public static final String MC_CONSOLE_WRITE = "mc.console.write";

    private Permissions() {
    }
}
