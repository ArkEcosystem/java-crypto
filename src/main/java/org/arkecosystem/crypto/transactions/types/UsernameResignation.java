package org.arkecosystem.crypto.transactions.types;

import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class UsernameResignation extends AbstractTransaction {

    public UsernameResignation() {
        super();
    }

    public UsernameResignation(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getPayload() {
        try {
            return new AbiEncoder(ContractAbiType.USERNAMES)
                    .encodeFunctionCall(AbiFunction.USERNAME_RESIGNATION.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding username resignation call", e);
        }
    }
}
