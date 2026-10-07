/// Data contract between Pixplaze and its Java clients (the Minecraft plugin).
///
/// ## Naming
///
/// The suffix names the role of a class; a bare name in Pixplaze code means its domain model.
///
/// - `*Info` — representation of an entity: [com.pixplaze.api.ext.data.server.MinecraftServerInfo],
///   [com.pixplaze.api.ext.data.player.MinecraftPlayerInfo], …
/// - `*Response` — result of an operation that is not an entity: device flow, errors.
/// - `*TokenInfo` — issued token pair, see [com.pixplaze.api.ext.data.auth.AuthorizationToken].
/// - `*Request` — request body that is not an entity representation.
/// - `*AuthorizationDetails` — subject-specific `authorization_details` of the device flow.
///
/// Field names follow the same rules everywhere:
///
/// - booleans start with `is`: `isLicense`, `isOnline`, `isOperator`;
/// - a reference to a Minecraft server is `minecraftServerId`;
/// - binary data is Base64 named after what it holds: `iconBase64`, `skinBase64`, `skinHeadBase64`;
/// - units are in the field documentation: durations in milliseconds, OAuth `expires_in`
///   and `interval` in seconds.
///
/// ## Views and `null`
///
/// One record serves several views of the same data. A frequently used view is a named static
/// factory on the record that fills only the fields of that view, e.g.
/// [com.pixplaze.api.ext.data.server.MinecraftServerInfo#heartbeat],
/// [com.pixplaze.api.ext.data.server.MinecraftServerInfo#preview],
/// [com.pixplaze.api.ext.data.server.MinecraftServerStateInfo#heartbeat]. Prefer such a factory
/// to a builder when sending data.
///
/// Hence `null` means "not part of this view or unknown", never "false", "zero" or "empty".
/// Fields the receiver does not take from a given view are listed in the factory documentation
/// and ignored if present.
///
/// ## JSON
///
/// Field names are camelCase, except the OAuth endpoint responses, which are snake_case as the RFCs
/// require: the types in [com.pixplaze.api.ext.data.oauth] and the token pairs returned by the token
/// endpoint (the same token pairs are camelCase in application sign-in responses). The library carries no JSON annotations: the naming
/// strategy is set by the JSON library of each side.
///
/// ## Compatibility
///
/// Adding a field or an enum value is compatible; renaming or removing one is not. Clients must
/// ignore unknown fields and read an unknown enum value as `null`.
package com.pixplaze.api.ext.data;
