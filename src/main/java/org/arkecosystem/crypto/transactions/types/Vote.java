package org.arkecosystem.crypto.transactions.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class Vote extends AbstractTransaction {
    public Vote() {
        super();
    }

    public Vote(Map<String, Object> data) {
        super();
        List<Object> payload = decodePayload(data);
        if (payload != null && !payload.isEmpty()) {
            Object arg = payload.get(0);
            this.vote = arg.toString();
        }
    }

    @Override
    public String getPayload() {
        if (this.vote == null || this.vote.isEmpty()) {
            return "";
        }

        List<Object> args = new ArrayList<>();
        args.add(this.vote);

        try {
            return new AbiEncoder().encodeFunctionCall(AbiFunction.VOTE.toString(), args);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding function call", e);
        }
    }
}
