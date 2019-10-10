package org.arkecosystem.crypto.enums;

public enum CoreTransactionTypes {
    TRANSFER(0),
    SECOND_SIGNATURE_REGISTRATION(1),
    DELEGATE_REGISTRATION(2),
    VOTE(3),
    MULTI_SIGNATURE_REGISTRATION(4),
    IPFS(5),
    MULTI_PAYMENT(6),
    DELEGATE_RESIGNATION(7),
    HTLC_LOCK(8),
    HTLC_CLAIM(9),
    HTLC_REFUND(10);

    private final int value;

    CoreTransactionTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
