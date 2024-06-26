package com.jabar.app.fitnesscenter.feature.user.entity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ERole {
    SUPER_ADMIN("Super Admin"),
    USER("User");

    private final String roleName;

    ERole(String s) {
        roleName = s;
    }


    public static ERole valueOfLabel(String label) {
        for (ERole e : values()) {
            if (e.roleName.equals(label)) {
                return e;
            }
        }
        return null;
    }

    @JsonCreator
    public static ERole decode(final String roleName) {
        return Stream.of(ERole.values())
                .filter(targetEnum -> targetEnum
                        .roleName
                        .equals(roleName))
                .findFirst()
                .orElse(null);
    }

    @JsonValue
    public String getRoleName() {
        return roleName;
    }
    @JsonValue
    @Override
    public String toString() {
        return roleName;
    }
}
