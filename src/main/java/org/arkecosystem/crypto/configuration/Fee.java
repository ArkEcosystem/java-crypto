package org.arkecosystem.crypto.configuration;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.enums.TransactionType;

import java.util.HashMap;
import java.util.Map;

public class Fee {
    private static Map<TransactionType, Long> fees = new HashMap<>();

    static {
        fees.put(TransactionType.TRANSFER, Fees.TRANSFER.getValue());
        fees.put(TransactionType.SECOND_SIGNATURE_REGISTRATION, Fees.SECOND_SIGNATURE_REGISTRATION.getValue());
        fees.put(TransactionType.DELEGATE_REGISTRATION, Fees.DELEGATE_REGISTRATION.getValue());
        fees.put(TransactionType.VOTE, Fees.VOTE.getValue());
        fees.put(TransactionType.MULTI_SIGNATURE_REGISTRATION, Fees.MULTI_SIGNATURE_REGISTRATION.getValue());
        fees.put(TransactionType.IPFS, Fees.IPFS.getValue());
        fees.put(TransactionType.MULTI_PAYMENT, Fees.MULTI_PAYMENT.getValue());
        fees.put(TransactionType.DELEGATE_RESIGNATION, Fees.DELEGATE_RESIGNATION.getValue());
    }

    public static long get(TransactionType type) {
        return fees.get(type);
    }

    public static void set(TransactionType type, long fee) {
        fees.put(type, fee);
    }
}
