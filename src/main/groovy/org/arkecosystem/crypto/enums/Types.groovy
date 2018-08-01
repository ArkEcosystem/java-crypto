package org.arkecosystem.crypto.enums

enum Types
{
    TRANSFER(0),
    SECOND_SIGNATURE_REGISTRATION(1),
    DELEGATE_REGISTRATION(2),
    VOTE(3),
    MULTI_SIGNATURE_REGISTRATION(4),
    IPFS(5),
    TIMELOCK_TRANSFER(6),
    MULTI_PAYMENT(7),
    DELEGATE_RESIGNATION(8)

    Types(Integer value) {
        this.value = value
    }

    private final Integer value

    Integer getValue() {
        value
    }
}
