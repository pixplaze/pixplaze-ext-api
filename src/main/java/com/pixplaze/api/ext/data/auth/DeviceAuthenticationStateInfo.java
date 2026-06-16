package com.pixplaze.api.ext.data.auth;

public record DeviceAuthenticationStateInfo<T>(
        Status status,
        T profile
) {

    public enum Status {
        PENDING,
        APPROVED,
        DENIED;

        public String code() {
            return name().toLowerCase();
        }
    }

    public DeviceAuthenticationStateInfo() {
        this(Status.PENDING, null);
    }

    public DeviceAuthenticationStateInfo(T profile) {
        this(Status.APPROVED, profile);
    }

    public DeviceAuthenticationStateInfo<T> denied() {
        return new DeviceAuthenticationStateInfo<>(Status.DENIED, null);
    }
}
