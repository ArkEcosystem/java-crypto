package org.arkecosystem.crypto.transactions.types;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class UsernameRegistration extends AbstractTransaction {

    public UsernameRegistration() {
        super();
    }

    public UsernameRegistration(Map<String, Object> data) {
        super(data);

        List<Object> payload = decodeUsernamePayload(data);
        if (payload != null && !payload.isEmpty()) {
            this.username = payload.get(0).toString();
        }
    }

    @Override
    public String getPayload() {
        if (this.username == null || this.username.isEmpty()) {
            return "";
        }

        try {
            return new AbiEncoder(ContractAbiType.USERNAMES)
                    .encodeFunctionCall(
                            AbiFunction.USERNAME_REGISTRATION.toString(),
                            Collections.singletonList(this.username));
        } catch (Exception e) {
            throw new RuntimeException("Error encoding username registration call", e);
        }
    }

    private static List<Object> decodeUsernamePayload(Map<String, Object> data) {
        if (data == null || !data.containsKey("data")) return null;

        String payload = (String) data.get("data");
        if (payload == null || payload.isEmpty()) return null;

        try {
            AbiDecoder decoder = new AbiDecoder(ContractAbiType.USERNAMES);
            Map<String, Object> decoded = decoder.decodeFunctionData(payload);
            return (List<Object>) decoded.get("args");
        } catch (Exception e) {
            return null;
        }
    }
}
