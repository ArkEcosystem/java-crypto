package org.arkecosystem.crypto.enums

enum Fees
{
    TRANSFER(10000000),
    SECOND_SIGNATURE_REGISTRATION(500000000),
    DELEGATE_REGISTRATION(2500000000),
    VOTE(100000000),
    MULTI_SIGNATURE_REGISTRATION(500000000),
    IPFS(0),
    TIMELOCK_TRANSFER(0),
    MULTI_PAYMENT(0),
    DELEGATE_RESIGNATION(0)

    Fees(Long value) {
        this.value = value
    }

    private final Long value

    Long getValue() {
        value
    }
}
