package org.arkecosystem.crypto.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionAsset {
    public Signature signature = new Signature();
    public List<String> votes = new ArrayList<>();
    public List<String> unvotes = new ArrayList<>();
    public Delegate delegate = new Delegate();
    public MultiSignature multiSignature = new MultiSignature();
    public MultiPayment multiPayment = new MultiPayment();
    public HashMap<String, Object> customAsset = new HashMap<>();
    public long amount = 0L;

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
}
