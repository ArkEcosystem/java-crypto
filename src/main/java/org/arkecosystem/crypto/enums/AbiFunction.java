package org.arkecosystem.crypto.enums;

public enum AbiFunction {
    VOTE("vote"),
    UNVOTE("unvote"),
    VALIDATOR_REGISTRATION("registerValidator"),
    VALIDATOR_RESIGNATION("resignValidator"),
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
