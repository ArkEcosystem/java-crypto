package org.arkecosystem.crypto.transactions.types;

import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class ValidatorResignation extends AbstractTransaction {
    public ValidatorResignation() {
        super();
    }

    public ValidatorResignation(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getPayload() {
        try {
            return new AbiEncoder()
                    .encodeFunctionCall(AbiFunction.VALIDATOR_RESIGNATION.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
