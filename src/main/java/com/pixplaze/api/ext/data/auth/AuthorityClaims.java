package com.pixplaze.api.ext.data.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/// Codec [Authority] ↔ access-token claims, shared by the issuer (web-api) and the verifiers
/// (web-api, plugin). Works on a plain `Map<String, Object>`, so it does not depend on a JWT
/// library: pass the claims map of whatever library verified the signature.
///
/// Only roles (`rls`), source (`src`) and audience (`aud`) travel in a token; permissions never
/// do — web-api expands roles itself, the plugin checks roles and the live state of the server.
public final class AuthorityClaims {

    private AuthorityClaims() {
    }

    /// Writes roles (`rls`, list of [Authority.Role#code]) and source (`src`, [Authority.Source#code]).
    /// The audience is not written here: JWT libraries set `aud` through their own API, take it
    /// from [Authority#targets()].
    public static void write(Map<String, Object> claims, Authority authority) {
        claims.put(Authority.Claims.ROLE, authority.roles().stream().map(Authority.Role::code).toList());
        claims.put(Authority.Claims.SOURCE, authority.source().code());
    }

    /// Restores an [Authority] from verified claims: roles, source and audience, no permissions.
    /// `aud` is accepted both as a single string and as a collection.
    ///
    /// @throws IllegalArgumentException if `rls` is missing or not a list
    /// @throws IllegalStateException    if a role code is unknown or `aud` is empty
    public static Authority read(Map<String, ?> claims) {
        if (!(claims.get(Authority.Claims.ROLE) instanceof Collection<?> roleCodes)) {
            throw new IllegalArgumentException("Claim '" + Authority.Claims.ROLE + "' must be a list of role codes");
        }

        final var roles = roleCodes.stream()
                .map(code -> Authority.Role.of(String.valueOf(code)))
                .toArray(Authority.Role[]::new);
        final var source = claims.get(Authority.Claims.SOURCE);
        return Authority
                .as(roles)
                .from(Authority.Source.of(source == null ? null : String.valueOf(source)))
                .to(audience(claims.get(Authority.Claims.TARGET)))
                .grant();
    }

    private static List<String> audience(Object aud) {
        if (aud instanceof String single) {
            return List.of(single);
        }

        final var targets = new ArrayList<String>();
        if (aud instanceof Collection<?> many) {
            many.forEach(target -> targets.add(String.valueOf(target)));
        }
        return targets;
    }
}
