package org.arkecosystem.crypto.transactions;

import java.util.HashMap;

public class TransactionAsset {
    public EvmCall evmCall = new EvmCall();  // Instance of EvmCall
    public String vote = "";                 // Optional "vote" property in hexadecimal format

    public static class EvmCall {
        public long gasLimit = 1000000; // Default gas limit
        public String payload = "";     // EVM code in hexadecimal format

        // Converts the EvmCall object to a HashMap for serialization
        public HashMap<String, Object> toHashMap() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("gasLimit", this.gasLimit);
            map.put("payload", this.payload);
            return map;
        }
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        
        // Adds "evmCall" to the map if it's defined
        if (evmCall != null) {
            map.put("evmCall", evmCall.toHashMap());
        }

        // Adds "vote" to the map if it's not empty
        if (vote != null && !vote.isEmpty()) {
            map.put("vote", this.vote);
        }

        return map;
    }
}
