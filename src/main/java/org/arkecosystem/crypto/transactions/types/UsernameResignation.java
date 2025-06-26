package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class UsernameResignation extends AbstractTransaction {
    public UsernameResignation() {
        super(); // Call the default constructor of AbstractTransaction
    }

    public UsernameResignation(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getPayload() {
        try {
            return new AbiEncoder()
                    .encodeFunctionCall(AbiFunction.USERNAME_RESIGNATION.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
