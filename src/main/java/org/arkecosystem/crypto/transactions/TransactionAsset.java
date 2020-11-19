package org.arkecosystem.crypto.transactions;

import org.arkecosystem.crypto.enums.HtlcLockExpirationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionAsset {
    public Signature signature = new Signature();
    public List<String> votes = new ArrayList<>();
    public Delegate delegate = new Delegate();
    public MultiSignature multiSignature = new MultiSignature();
    public MultiPayment multiPayment = new MultiPayment();
    public String ipfs;
    public HtlcLockAsset htlcLockAsset = new HtlcLockAsset();
    public HtlcClaimAsset htlcClaimAsset = new HtlcClaimAsset();
    public HtlcRefundAsset htlcRefundAsset = new HtlcRefundAsset();
    public HashMap<String, Object> customAsset = new HashMap<>();

    public static class Signature {
        public String publicKey;
    }

    public static class Delegate {
        public String username;
    }

    public static class MultiSignature {
        public byte min;
        public List<String> publicKeys = new ArrayList<>();
    }

    public static class MultiPayment {
        public List<Payment> payments = new ArrayList<>();
    }

    public static class Payment {
        public long amount;
        public String recipientId;

        public Payment(long amount, String recipientId) {
            this.amount = amount;
            this.recipientId = recipientId;
        }
    }

    public static class HtlcLockAsset {
        public String secretHash;
        public Expiration expiration;
    }

    public static class Expiration {
        public HtlcLockExpirationType type;
        public int value;

        public Expiration(HtlcLockExpirationType type, int value) {
            this.type = type;
            this.value = value;
        }
    }

    public static class HtlcClaimAsset {
        public String lockTransactionId;
        public String unlockSecret;
    }

    public static class HtlcRefundAsset {
        public String lockTransactionId;
    }
}
