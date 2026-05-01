package org.arkecosystem.crypto.enums;

public enum AbiFunction {
    VOTE("vote"),
    UNVOTE("unvote"),
    VALIDATOR_REGISTRATION("registerValidator"),
    VALIDATOR_RESIGNATION("resignValidator"),
    USERNAME_REGISTRATION("registerUsername"),
    USERNAME_RESIGNATION("resignUsername"),
    MULTIPAYMENT("pay"),
    TRANSFER("transfer"),
    APPROVE("approve");

    private final String functionName;

    AbiFunction(String functionName) {
        this.functionName = functionName;
    }

    public String toString() {
        return functionName;
    }
}
