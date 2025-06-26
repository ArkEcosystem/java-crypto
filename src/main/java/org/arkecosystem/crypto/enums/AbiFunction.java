package org.arkecosystem.crypto.enums;

public enum AbiFunction {
    VOTE("vote"),
    UNVOTE("unvote"),
    USERNAME_REGISTRATION("registerUsername"),
    USERNAME_RESIGNATION("resignUsername"),
    VALIDATOR_REGISTRATION("registerValidator"),
    VALIDATOR_RESIGNATION("resignValidator");

    private final String functionName;

    AbiFunction(String functionName) {
        this.functionName = functionName;
    }

    public String toString() {
        return functionName;
    }
}
