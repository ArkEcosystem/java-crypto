package org.arkecosystem.crypto.configuration


import org.arkecosystem.crypto.enums.Fees
import org.arkecosystem.crypto.enums.Types

class Fee {
    private static Map<Integer, Long> fees

    static
    {
        fees = new HashMap<Integer, Long>()
        fees.put(Types.TRANSFER.getValue(), Fees.TRANSFER.getValue())
        fees.put(Types.SECOND_SIGNATURE_REGISTRATION.getValue(), Fees.SECOND_SIGNATURE_REGISTRATION.getValue())
        fees.put(Types.DELEGATE_REGISTRATION.getValue(), Fees.DELEGATE_REGISTRATION.getValue())
        fees.put(Types.VOTE.getValue(), Fees.VOTE.getValue())
        fees.put(Types.MULTI_SIGNATURE_REGISTRATION.getValue(), Fees.MULTI_SIGNATURE_REGISTRATION.getValue())
        fees.put(Types.IPFS.getValue(), Fees.IPFS.getValue())
        fees.put(Types.TIMELOCK_TRANSFER.getValue(), Fees.TIMELOCK_TRANSFER.getValue())
        fees.put(Types.MULTI_PAYMENT.getValue(), Fees.MULTI_PAYMENT.getValue())
        fees.put(Types.DELEGATE_RESIGNATION.getValue(), Fees.DELEGATE_RESIGNATION.getValue())
    }

    static long get(int type) {
        return fees[type]
    }

    static void set(int type, long fee) {
        fees.replace(type, fee)
    }
}
