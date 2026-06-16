package com.pixplaze.api.ext.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Authority {

    public enum Role {
        USER,
        APPLICATION,
        ADMINISTRATOR;
    }

    public enum Source {
        NOT_AUTHORIZED_DEVICE("NAD"),
        APPLICATION_AUTHORIZED_DEVICE("AAD"),
        MINECRAFT_AUTHORIZED_DEVICE("MAD");

        private final String code;

        Source(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }

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

        public static Source of(int code) {
            return switch (code) {
                case 1 -> APPLICATION_AUTHORIZED_DEVICE;
                case 2 -> MINECRAFT_AUTHORIZED_DEVICE;
                default -> NOT_AUTHORIZED_DEVICE;
            };
        }
    }

    public enum Target {
        PIXPLAZE,
        MINECRAFT
    }

    public static class Builder {
        final Role role;
        Source source;
        List<Target> targets;

        Builder(Role role) {
            this.role = role;
            this.targets = new ArrayList<>();
        }

        public Builder from(Source source) {
            this.source = source;
            return this;
        }

        public Builder to(Target ... target) {
            this.targets.addAll(Arrays.stream(target).filter(Objects::nonNull).toList());
            return this;
        }

        public Authority withoutPermissions() {
            return new Authority(
                    validateRole(role),
                    validateSource(source),
                    validateTargets(targets),
                    List.of()
            );
        }

        public Authority withDefaultPermissions() {
            return new Authority(
                    validateRole(role),
                    validateSource(source),
                    validateTargets(targets),
                    List.of()
            );
        }

        public Authority withPermissions(List<String> permissions) {
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

    public static Builder as(Role role) {
        return new Builder(role);
    }

    public List<String> describe() {
        final var authorities = new ArrayList<String>();
        authorities.add("ROLE_" + role.name());
        authorities.addAll(targets.stream().map(t -> "TARGET_" + t.name()).toList());
        authorities.add("SOURCE_" + source.name());
        authorities.addAll(permissions);
        return authorities;
    }

    public static void main(String[] args) {
        final var authority = Authority.as(Role.USER)
                .from(Source.MINECRAFT_AUTHORIZED_DEVICE)
                .to(Target.MINECRAFT, Target.PIXPLAZE)
                .withDefaultPermissions();
        System.out.println(authority.describe());
    }
}
