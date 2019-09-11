package org.arkecosystem.crypto.enums;

public enum TransactionTypeGroup {
    TEST(0),
    CORE(1);

    private final int value;

    TransactionTypeGroup(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
