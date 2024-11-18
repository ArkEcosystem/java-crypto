package org.arkecosystem.crypto.enums;

import org.arkecosystem.crypto.transactions.types.Unvote;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;
import org.arkecosystem.crypto.transactions.types.ValidatorResignation;
import org.arkecosystem.crypto.transactions.types.Vote;

public enum AbiFunction {
    VOTE("vote"),
    UNVOTE("unvote"),
    VALIDATOR_REGISTRATION("registerValidator"),
    VALIDATOR_RESIGNATION("resignValidator");

    private final String functionName;

    AbiFunction(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Class<?> transactionClass() {
        switch (this) {
            case VOTE:
                return Vote.class;
            case UNVOTE:
                return Unvote.class;
            case VALIDATOR_REGISTRATION:
                return ValidatorRegistration.class;
            case VALIDATOR_RESIGNATION:
                return ValidatorResignation.class;
            default:
                throw new IllegalArgumentException("Unknown AbiFunction: " + this);
        }
    }
}
