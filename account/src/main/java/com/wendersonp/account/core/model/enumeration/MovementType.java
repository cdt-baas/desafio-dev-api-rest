package com.wendersonp.account.core.model.enumeration;

public enum MovementType {

    WITHDRAW("W"), DEPOSIT("D");

    private final String value;

    MovementType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static MovementType fromValue(String value) {
        for (MovementType movementType : MovementType.values()) {
            if (movementType.value().equals(value)) {
                return movementType;
            }
        }
        return null;
    }
}
