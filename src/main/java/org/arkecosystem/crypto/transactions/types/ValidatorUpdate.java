package org.arkecosystem.crypto.transactions.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class ValidatorUpdate extends AbstractTransaction {
    public ValidatorUpdate() {
        super();
    }

    public ValidatorUpdate(Map<String, Object> data) {
        super(data);

        List<Object> payload = decodePayload(data);
        if (payload != null && payload.size() >= 2) {
            this.validatorPublicKey = payload.get(0).toString().replaceFirst("^0x", "");
            this.validatorProof = payload.get(1).toString().replaceFirst("^0x", "");
        }
    }

    @Override
    public String getPayload() {
        if (this.validatorPublicKey == null
                || this.validatorPublicKey.isEmpty()
                || this.validatorProof == null
                || this.validatorProof.isEmpty()) {
            return "";
        }

        List<Object> args = new ArrayList<>();
        args.add("0x" + this.validatorPublicKey);
        args.add("0x" + this.validatorProof);

        try {
            return new AbiEncoder()
                    .encodeFunctionCall(AbiFunction.UPDATE_VALIDATOR.toString(), args);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
