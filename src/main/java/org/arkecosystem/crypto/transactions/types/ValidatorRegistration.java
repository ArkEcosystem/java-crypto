package org.arkecosystem.crypto.transactions.types;

import java.util.ArrayList;
import java.util.HashMap;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class ValidatorRegistration extends AbstractTransaction {
    @Override
    public String getPayload() {
        try {
            AbiEncoder abiEncoder = new AbiEncoder();
            ArrayList<Object> args = new ArrayList<>();
            args.add(this.asset.validatorPublicKey);
            return abiEncoder.encodeFunctionCall("registerValidator", args);
        } catch (Exception e) {
            e.printStackTrace();
            
        }

        return "";   
    }
    
    @Override
    public HashMap<String, Object> assetToHashMap() {
        return null;
    }
}
