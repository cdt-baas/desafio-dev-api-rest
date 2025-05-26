package com.wendersonp.account.core.model.enumeration;

public enum BlockStatus {
    BLOCKED("B"), UNBLOCKED("U"), CLOSED("C");

    private final String value;

    BlockStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static BlockStatus fromValue(String value) {
        for (BlockStatus blockStatus : BlockStatus.values()) {
            if (blockStatus.value().equals(value)) {
                return blockStatus;
            }
        }
        throw new IllegalArgumentException("Invalid block status value: " + value);
    }
}
