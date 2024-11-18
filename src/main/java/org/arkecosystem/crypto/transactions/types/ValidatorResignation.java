package org.arkecosystem.crypto.transactions.types;

import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class ValidatorResignation extends AbstractTransaction {
    @Override
    public String getPayload() {
        try {
            return new AbiEncoder().encodeFunctionCall(AbiFunction.VALIDATOR_RESIGNATION.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
