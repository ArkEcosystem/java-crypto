package org.arkecosystem.crypto.transactions;

import org.arkecosystem.crypto.enums.HtlcLockExpirationType;

import java.util.*;

public class TransactionAsset {
    public Signature signature = new Signature();
    public List<String> votes = new ArrayList<>();
    public Delegate delegate = new Delegate();
    public MultiSignature multisignature = new MultiSignature();
    public MultiPayment multiPayment = new MultiPayment();
    public String ipfs;
    public HtlcLockAsset htlcLockAsset = new HtlcLockAsset();
    public HtlcClaimAsset htlcClaimAsset = new HtlcClaimAsset();
    public HtlcRefundAsset htlcRefundAsset = new HtlcRefundAsset();


    public class Signature {
        public String publicKey;
    }

    public class Delegate {
        public String username;
    }

    public class MultiSignature {
        public byte min;
        public byte lifetime;
        public List<String> keysgroup = new ArrayList<>();
    }

    public  class MultiPayment{
        public List <Payment> payments = new ArrayList<>();
    }

    public static class Payment {
        public long amount;
        public String recipientId;
        public Payment(long amount, String recipientId){
            this.amount = amount;
            this.recipientId = recipientId;
        }
    }

    public class HtlcLockAsset {
        public String secretHash;
        public Expiration expiration;
    }

    public static class Expiration{
        public HtlcLockExpirationType type;
        public int value;

        public Expiration(HtlcLockExpirationType type, int value){
            this.type = type;
            this.value = value;
        }
    }

    public class HtlcClaimAsset{
        public String lockTransactionId;
        public String unlockSecret;
    }

    public class HtlcRefundAsset{
        public String lockTransactionId;
    }

}
