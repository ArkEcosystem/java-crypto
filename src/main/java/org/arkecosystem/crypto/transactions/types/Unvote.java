package org.arkecosystem.crypto.transactions.types;

import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class Unvote extends AbstractTransaction {

    public Unvote() {
        super();
    }

    public Unvote(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getPayload() {
        try {
            return new AbiEncoder().encodeFunctionCall(AbiFunction.UNVOTE.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
