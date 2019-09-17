package org.arkecosystem.crypto.enums;

public enum HtlcLockExpirationType {
    UNIX_TIMESTAMP(1),
    BLOCK_HEIGHT(2);

    private final int value;

    HtlcLockExpirationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
