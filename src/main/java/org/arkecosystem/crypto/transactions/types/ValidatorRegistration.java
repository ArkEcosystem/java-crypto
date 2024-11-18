package org.arkecosystem.crypto.transactions.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class ValidatorRegistration extends AbstractTransaction {
    public ValidatorRegistration() {
        super(); // Call the default constructor of AbstractTransaction
    }

    public ValidatorRegistration(Map<String, Object> data) {
        super(data);
        
        // Use a local decodePayload method since we can't rely on AbstractTransaction's data field
        List<Object> payload = decodePayload(data);
        if (payload != null && !payload.isEmpty()) {
            Object arg = payload.get(0);
            this.validatorPublicKey = arg.toString().replaceFirst("^0x", "");
        }
    }

    @Override
    public String getPayload() {
        if (this.validatorPublicKey == null || this.validatorPublicKey.isEmpty()) {
            return "";
        }

        String validatorPublicKeyHex = "0x" + this.validatorPublicKey;
        List<Object> args = new ArrayList<>();
        args.add(validatorPublicKeyHex);

        try {
            return new AbiEncoder()
                    .encodeFunctionCall(AbiFunction.VALIDATOR_REGISTRATION.toString(), args);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
