package org.arkecosystem.crypto.transactions;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.utils.TransactionUtils;

public class Serializer {
    private final AbstractTransaction transaction;

    private Serializer(AbstractTransaction transaction) {
        this.transaction = transaction;
    }

    public static Serializer newSerializer(AbstractTransaction transaction) {
        return new Serializer(transaction);
    }

    public static byte[] getBytes(AbstractTransaction transaction, boolean skipSignature) {
        return new Serializer(transaction).serialize(skipSignature);
    }

    public byte[] serialize(boolean skipSignature) {
        return TransactionUtils.toBuffer(transaction.toHashMap(), skipSignature);
    }
}
