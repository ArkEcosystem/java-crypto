package org.arkecosystem.crypto.enums;

public enum CoreTransactionTypes {
    TRANSFER(0),
    SECOND_SIGNATURE_REGISTRATION(1),
    VALIDATOR_REGISTRATION(2),
    VOTE(3),
    MULTI_SIGNATURE_REGISTRATION(4),
    MULTI_PAYMENT(6),
    VALIDATOR_RESIGNATION(7),
    USERNAME_REGISTRATION(8),
    USERNAME_RESIGNATION(9);

    private final int value;

    CoreTransactionTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
