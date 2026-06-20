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
/// - [Target] `targets` — **set** of zones the token is valid for (*where it may
///   act*); plural, because one token may address several resource servers
///   (e.g. an `AAD` token covers both [Target#MINECRAFT] and [Target#PIXPLAZE]).
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
    /// the standard `aud` (audience) claim — a [Target] *is* the token's audience.
    public static class Claims {
        public static final String ROLE = "role";
        public static final String SOURCE = "src";
        public static final String TARGET = "aud";
        public static final String PERMISSIONS = "perms";
        public static final String MINECRAFT_CONTEXT = "mc";
    }

    /// Privilege tier of the subject (the *who*).
    public enum Role {
        /// Regular player / profile owner.
        USER,
        /// Service (machine-to-machine) client — the Minecraft server itself.
        APPLICATION,
        /// Elevated subject — server operator / system administrator.
        ADMINISTRATOR;
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
                case 0 -> NOT_AUTHORIZED_DEVICE;
                case 1 -> APPLICATION_AUTHORIZED_DEVICE;
                case 2 -> MINECRAFT_AUTHORIZED_DEVICE;
                default -> NOT_AUTHORIZED_DEVICE;
            };
        }
    }

    /// Audience zone the token is valid for (the *where to*) — materialized as the
    public enum Target {
        /// Backend / BFF: profiles, bindings, billing, server management.
        PIXPLAZE,
        /// Minecraft server (REST/WS): chat, console, commands, player/plugin lists.
        MINECRAFT
    }

    /// Fluent builder for [Authority]. Start from [Authority#as], set the provenance
    /// with [#from] and the zones with [#to], then finish with one of the
    /// `with*Permissions` methods. `role`, `source` and at least one `target` are
    /// mandatory and validated on build.
    public static class Builder {
        final Role role;
        Source source;
        List<Target> targets;
        List<String> permissions;

        Builder(Role role) {
            this.role = role;
            this.targets = new ArrayList<>();
        }

        /// Sets the token [Source] (provenance).
        public Builder from(Source source) {
            this.source = source;
            return this;
        }

        public Builder to(List<Target> targets) {
            this.targets.addAll(targets.stream().filter(Objects::nonNull).toList());
            return this;
        }

        /// Adds one or more [Target] zones; `null` entries are ignored.
        public Builder to(Target ... target) {
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
                    validateRole(role),
                    validateSource(source),
                    List.of(),
                    List.of()
            );
        }

        /// Builds an [Authority] with the role's default permission set.
        /// TODO: currently identical to [#unauthorized] — no defaults are wired up yet.
        public Authority withDefaultPermissions() {
            return new Authority(
                    validateRole(role),
                    validateSource(source),
                    validateTargets(targets),
                    List.of()
            );
        }

        /// Builds an [Authority] with the given fine-grained permissions.
        public Authority grant(String ... permissions) {
            return grant(Arrays.asList(permissions));
        }

        /// Builds an [Authority] with the given fine-grained permissions.
        public Authority grant(List<String> permissions) {
            return new Authority(
                    validateRole(role),
                    validateSource(source),
                    validateTargets(targets),
                    validatePermissions(permissions)
            );
        }

        private Role validateRole(Role role) {
            if (role == null) {
                throw new IllegalStateException("Role must be set by 'as(Role)'!");
            }

            return role;
        }

        private Source validateSource(Source source) {
            if (source == null) {
                throw new IllegalStateException("Source must be set by 'from(Source)'!");
            }

            return source;
        }

        private List<Target> validateTargets(List<Target> targets) {
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

    private final Role role;
    private final Source source;
    private final List<Target> targets;
    private final List<String> permissions;

    private Authority(Role role, Source source, List<Target> targets, List<String> permissions) {
        this.role = Objects.requireNonNull(role, "role must not be null!");
        this.source = Objects.requireNonNull(source, "source must not be null!");
        this.targets = Objects.requireNonNull(targets, "targets must not be null!");
        this.permissions = Objects.requireNonNull(permissions, "permissions must not be null!");
    }

    /// Starts building an [Authority] for the given [Role].
    public static Builder as(Role role) {
        return new Builder(role);
    }

    /// Starts building an [Authority] from the given [Authority]
    public static Builder as(Authority authority) {
        return new Builder(authority.role)
                .from(authority.source)
                .to(authority.targets)
                .permissions(authority.permissions);
    }

    /// Returns `true` if this authority's role equals `role`.
    public boolean is(Role role) {
        return this.role.equals(role);
    }

    /// Returns `true` if `target` is among this authority's zones.
    public boolean to(Target target) {
        return targets.contains(target);
    }

    /// Returns `true` if this authority was issued from `source`.
    public boolean from(Source source) {
        return this.source.equals(source);
    }

    /// Returns `true` if this authority matches all three *zone* dimensions:
    /// `is(role) && from(source) && to(target)`. Does **not** consider
    /// `permissions` — check those separately (see class doc).
    public boolean satisfies(Role role, Source source, Target target) {
        return is(role) && from(source) && to(target);
    }

    /// Flattens this authority into Spring Security authority strings:
    /// `ROLE_<role>`, one `TARGET_<target>` per zone, `SOURCE_<source>`, plus the
    /// raw `permissions`.
    public List<String> describe() {
        final var authorities = new ArrayList<String>();
        authorities.add("ROLE_" + role.name());
        authorities.add("SOURCE_" + source.name());
        authorities.addAll(targets.stream().map(t -> "TARGET_" + t.name()).toList());
        authorities.addAll(permissions);
        return authorities;
    }
}
