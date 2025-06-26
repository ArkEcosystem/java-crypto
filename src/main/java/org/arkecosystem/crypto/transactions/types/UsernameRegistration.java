package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class UsernameRegistration extends AbstractTransaction {
    public UsernameRegistration() {
        super(); // Call the default constructor of AbstractTransaction
    }

    public UsernameRegistration(Map<String, Object> data) {
        super(data);

        // Use a local decodePayload method since we can't rely on AbstractTransaction's data field
        List<Object> payload = decodePayload(data);
        if (payload != null && !payload.isEmpty()) {
            Object arg = payload.get(0);
            this.username = arg.toString();
        }
    }

    @Override
    public String getPayload() {
        if (this.username == null || this.username.isEmpty()) {
            return "";
        }

        List<Object> args = new ArrayList<>();
        args.add(this.username);

        try {
            return new AbiEncoder()
                    .encodeFunctionCall(AbiFunction.USERNAME_REGISTRATION.toString(), args);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
