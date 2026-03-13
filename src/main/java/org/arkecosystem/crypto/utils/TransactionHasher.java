package org.arkecosystem.crypto.utils;

import java.util.Map;

public class TransactionHasher {

    public static byte[] toHash(Map<String, Object> transaction, boolean skipSignature) {
        return TransactionUtils.toHash(transaction, skipSignature);
    }
}
