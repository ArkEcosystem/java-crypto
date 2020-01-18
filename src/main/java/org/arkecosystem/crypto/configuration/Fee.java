package org.arkecosystem.crypto.configuration;

import java.util.HashMap;
import java.util.Map;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class Fee {
    private static Map<Integer, Map<Integer, Long>> internalTransactionTypes = new HashMap<>();

    static {
        Map<Integer, Long> coreFees = new HashMap<>();

        coreFees.put(CoreTransactionTypes.TRANSFER.getValue(), Fees.TRANSFER.getValue());
        coreFees.put(
                CoreTransactionTypes.SECOND_SIGNATURE_REGISTRATION.getValue(),
                Fees.SECOND_SIGNATURE_REGISTRATION.getValue());
        coreFees.put(
                CoreTransactionTypes.DELEGATE_REGISTRATION.getValue(),
                Fees.DELEGATE_REGISTRATION.getValue());
        coreFees.put(CoreTransactionTypes.VOTE.getValue(), Fees.VOTE.getValue());
        coreFees.put(
                CoreTransactionTypes.MULTI_SIGNATURE_REGISTRATION.getValue(),
                Fees.MULTI_SIGNATURE_REGISTRATION.getValue());
        coreFees.put(CoreTransactionTypes.IPFS.getValue(), Fees.IPFS.getValue());
        coreFees.put(CoreTransactionTypes.MULTI_PAYMENT.getValue(), Fees.MULTI_PAYMENT.getValue());
        coreFees.put(
                CoreTransactionTypes.DELEGATE_RESIGNATION.getValue(),
                Fees.DELEGATE_RESIGNATION.getValue());
        coreFees.put(CoreTransactionTypes.HTLC_LOCK.getValue(), Fees.HTLC_LOCK.getValue());
        coreFees.put(CoreTransactionTypes.HTLC_CLAIM.getValue(), Fees.HTLC_CLAIM.getValue());
        coreFees.put(CoreTransactionTypes.HTLC_REFUND.getValue(), Fees.HTLC_REFUND.getValue());

        internalTransactionTypes.put(TransactionTypeGroup.CORE.getValue(), coreFees);
    }

    public static long getFee(int transactionTypeGroup, int type) {
        return internalTransactionTypes.get(transactionTypeGroup).get(type);
    }

    public static long getCoreFee(int type) {
        return internalTransactionTypes.get(TransactionTypeGroup.CORE.getValue()).get(type);
    }

    public static void set(int transactionTypeGroup, Map<Integer, Long> transactionTypes) {
        if (transactionTypeGroup < TransactionTypeGroup.RESERVED.getValue()) {
            throw new IllegalArgumentException(
                    "Cannot add TransactionTypeGroup with value less then 1000");
        }
        internalTransactionTypes.put(transactionTypeGroup, transactionTypes);
    }
}
