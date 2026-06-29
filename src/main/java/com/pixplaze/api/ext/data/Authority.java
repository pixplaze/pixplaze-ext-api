package com.pixplaze.api.ext.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/// Decoded authorization payload carried inside an access token (JWT) — the
/// "passport" of a request's subject. Bundles four independent dimensions:
///
/// - [Role] `role` — single privilege tier of the subject (*who*).
/// - [Source] `source` — single provenance of the token (*where it was issued*),
///   and with it the trust level.
///   Membership semantics: [#to] is satisfied if the zone is *among* the targets.
/// - `permissions` — **set** of fine-grained, namespaced rights (*what exactly*).
///
/// Built fluently via [#as]:
/// `Authority.as(role).from(source).to(targets...).withPermissions(...)`.
/// Queried with the mirrored predicates [#is], [#from], [#to] and the composite
/// [#satisfies].
///
/// **One concept, three vocabularies** — keep the mapping in mind:
///
/// | Java field   | JWT claim ([Claims]) | Spring authority ([#describe]) |
/// |--------------|----------------------|--------------------------------|
/// | `role`       | `role`               | `ROLE_*`                       |
/// | `source`     | `src`                | `SOURCE_*`                     |
/// | `targets`    | `aud`                | `TARGET_*`                     |
/// | `permissions`| `perms`              | (raw strings)                  |
public class Authority {

    /// JWT claim names for each [Authority] dimension. Note that `TARGET` maps to
    /// the standard `aud` (audience) claim — a *is* the token's audience.
    public static class Claims {
        public static final String ROLE = "rls";
        public static final String SOURCE = "src";
        public static final String TARGET = "aud";
        public static final String PERMISSIONS = "perms";
        public static final String MINECRAFT_CONTEXT = "mc";
    }

    /// Privilege tier of the subject (the *who*).
    public enum Role {
        USER("RUSR", List.of(
                "sys:profile.*",
                "sys:servers.rating.*",
                "sys:servers.list.read"
        )), // ROLE_USER
        ADMIN("RADM", List.of(
                "sys:profile.*"
        )), // ROLE_USER
        SYSTEM("RSYS", List.of(
                "sys:*"
        )), // ROLE_SYSTEM
        MINECRAFT_PLAYER("RMCP", List.of(
                "mc:${target}:server.chat.read",
                "mc:${target}:server.chat.write"
        )), // ROLE_MINECRAFT_PLAYER
        MINECRAFT_OPERATOR("RMCO", List.of(
                "mc:${target}:server.chat.read",
                "mc:${target}:server.chat.write",
                "mc:${target}:server.console.read",
                "mc:${target}:server.console.write"
        )), // ROLE_MINECRAFT_OPERATOR
        MINECRAFT_SERVER("RMCS", List.of(

        )); // ROLE_MINECRAFT_SERVER

        private final String code;
        private final List<String> permissions;

        Role(String code, List<String> permissions) {
            this.code = code;
            this.permissions = permissions;
        }

        public static Role of(String code) {
            return switch (code) {
                case "RUSR", "ROLE_USER", "USER" -> USER;
                case "RADM", "ROLE_ADMIN", "ADMIN" -> ADMIN;
                case "RSYS", "ROLE_SYSTEM", "SYSTEM" -> SYSTEM;
                case "RMCP", "ROLE_MINECRAFT_PLAYER", "MINECRAFT_PLAYER" -> MINECRAFT_PLAYER;
                case "RMCO", "ROLE_MINECRAFT_OPERATOR", "MINECRAFT_OPERATOR" -> MINECRAFT_OPERATOR;
                case "RMCS", "ROLE_MINECRAFT_SERVER", "MINECRAFT_SERVER" -> MINECRAFT_SERVER;
                default -> throw new IllegalStateException("Unexpected value: " + code);
            };
        }

        public String code() {
            return code;
        }
    }

    /// Provenance of the token (the *where from*) and, with it, the trust level.
    public enum Source {
        /// `NAD` — pre-auth device; only the device-authorization flow is allowed.
        NOT_AUTHORIZED_DEVICE("NAD"),
        /// `AAD` — issued on the BFF's initiative; authorizes incoming calls to Minecraft.
        APPLICATION_AUTHORIZED_DEVICE("AAD"),
        /// `MAD` — issued on the Minecraft server's initiative (RFC 8628); limited,
        /// calls the BFF only and does not take part in Minecraft-side checks.
        MINECRAFT_AUTHORIZED_DEVICE("MAD");

        private final String code;

        Source(String code) {
            this.code = code;
        }

        /// Short wire code, e.g. `"MAD"`.
        public String code() {
            return code;
        }

        /// Parses a [Source] from its short code or full enum name (case-insensitive).
        /// A `null` or unknown value falls back to [#NOT_AUTHORIZED_DEVICE].
        public static Source of(String code) {
            if (code == null) {
                return NOT_AUTHORIZED_DEVICE;
            }

            return switch (code.toUpperCase()) {
                case "MAD", "MINECRAFT_AUTHORIZED_DEVICE" -> MINECRAFT_AUTHORIZED_DEVICE;
                case "AAD", "APPLICATION_AUTHORIZED_DEVICE" -> APPLICATION_AUTHORIZED_DEVICE;
                default -> NOT_AUTHORIZED_DEVICE;
            };
        }

        /// Parses a [Source] from its numeric code (`0` = NAD, `1` = AAD, `2` = MAD).
        /// Any unknown value falls back to [#NOT_AUTHORIZED_DEVICE].
        public static Source of(int code) {
            return switch (code) {
                case 1 -> APPLICATION_AUTHORIZED_DEVICE;
                case 2 -> MINECRAFT_AUTHORIZED_DEVICE;
                default -> NOT_AUTHORIZED_DEVICE;
            };
        }
    }

    /// Fluent builder for [Authority]. Start from [Authority#as], set the provenance
    /// with [#from] and the zones with [#to], then finish with one of the
    /// `with*Permissions` methods. `role`, `source` and at least one `target` are
    /// mandatory and validated on build.
    public static class Builder {
        List<Role> roles;
        Source source;
        List<String> targets;
        List<String> permissions;

        Builder(Role ... roles) {
            this(Arrays.asList(roles));
        }

        Builder(List<Role> roles) {
            this.roles = roles;
            this.targets = new ArrayList<>();
            this.permissions = new ArrayList<>();
        }

        /// Sets the token [Source] (provenance).
        public Builder from(Source source) {
            this.source = source;
            return this;
        }

        public Builder to(List<String> targets) {
            this.targets.addAll(targets.stream().filter(Objects::nonNull).toList());
            return this;
        }

        /// Adds one or more target zones (audience); `null` entries are ignored.
        public Builder to(String ... target) {
            to(Arrays.asList(target));
            return this;
        }

        private Builder permissions(List<String> permissions) {
            this.permissions.addAll(permissions.stream().filter(Objects::nonNull).toList());
            return this;
        }

        /// Builds an [Authority] carrying no fine-grained permissions.
        public Authority unauthorized() {
            return new Authority(
                    validateRoles(roles),
                    validateSource(source),
                    List.of(),
                    List.of()
            );
        }

        /// Builds an [Authority] with the role's default permission set.
        /// TODO: currently identical to [#unauthorized] — no defaults are wired up yet.
        public Authority withDefaultPermissions() {
            return new Authority(
                    validateRoles(roles),
                    validateSource(source),
                    validateTargets(targets),
                    List.of()
            );
        }

        /// Builds an [Authority] with the given fine-grained permissions.
        public Authority grant(String ... permissions) {
            if (permissions.length == 0) {
                final var defaultPermissions = new ArrayList<>();
                for (var target : targets) {
                    if (roles.contains(Role.MINECRAFT_PLAYER)) {

                    }

                    if (roles.contains(Role.MINECRAFT_OPERATOR)) {

                    }
                }
            }
            return grant(Arrays.asList(permissions));
        }

        /// Builds an [Authority] with the given fine-grained permissions.
        public Authority grant(List<String> permissions) {
            return new Authority(
                    validateRoles(roles),
                    validateSource(source),
                    validateTargets(targets),
                    validatePermissions(permissions)
            );
        }

        private List<Role> validateRoles(List<Role> roles) {
            if (roles == null || roles.isEmpty()) {
                throw new IllegalStateException("Role must be set by 'as(Role)'!");
            }

            return roles;
        }

        private Source validateSource(Source source) {
            if (source == null) {
                throw new IllegalStateException("Source must be set by 'from(Source)'!");
            }

            return source;
        }

        private List<String> validateTargets(List<String> targets) {
            if (targets.isEmpty()) {
                throw new IllegalStateException("Targets must be set by 'to(Target)'!");
            }

            return targets;
        }

        private List<String> validatePermissions(List<String> permissions) {
            if (permissions == null) {
                throw new IllegalStateException("Permissions must set by 'withPermissions(List<String>)'");
            }

            return permissions;
        }
    }

    private final List<Role> roles;
    private final Source source;
    private final List<String> targets;
    private final List<String> permissions;

    private Authority(List<Role> roles, Source source, List<String> targets, List<String> permissions) {
        this.roles = Objects.requireNonNull(roles, "roles must not be null!");
        this.source = Objects.requireNonNull(source, "source must not be null!");
        this.targets = Objects.requireNonNull(targets, "targets must not be null!");
        this.permissions = Objects.requireNonNull(permissions, "permissions must not be null!");
    }

    /// Starts building an [Authority] for the given [Role].
    public static Builder as(Role ... roles) {
        return new Builder(roles);
    }

    /// Starts building an [Authority] from the given [Authority]
    public static Builder as(Authority authority) {
        return new Builder(authority.roles)
                .from(authority.source)
                .to(authority.targets)
                .permissions(authority.permissions);
    }

    public List<Role> roles() {
        return roles;
    }

    public Source source() {
        return source;
    }

    public List<String> targets() {
        return targets;
    }

    public List<String> permissions() {
        return permissions;
    }

    /// Returns `true` if this authority's role equals `role`.
    public boolean is(Role role) {
        return this.roles.contains(role);
    }

    /// Returns `true` if `target` is among this authority's zones.
    public boolean to(String target) {
        return targets.contains(target);
    }

    /// Returns `true` if this authority was issued from `source`.
    public boolean from(Source source) {
        return this.source.equals(source);
    }

    /// Returns `true` if this authority matches all three *zone* dimensions:
    /// `is(role) && from(source) && to(target)`. Does **not** consider
    /// `permissions` — check those separately (see class doc).
    public boolean satisfies(Role role, Source source, String target) {
        return is(role) && from(source) && to(target);
    }

    /// Flattens this authority into Spring Security authority strings:
    /// `ROLE_<role>`, one `TARGET_<target>` per zone, `SOURCE_<source>`, plus the
    /// raw `permissions`.
    public List<String> describe() {
        final var authorities = new ArrayList<>(roles.stream().map(r -> "ROLE_" + r).toList());
        authorities.add("SOURCE_" + source.name());
        authorities.addAll(targets.stream().map(t -> "TARGET_" + t).toList());
        authorities.addAll(permissions);
        return authorities;
    }
}
