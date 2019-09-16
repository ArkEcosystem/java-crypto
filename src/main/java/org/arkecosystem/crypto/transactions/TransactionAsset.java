package org.arkecosystem.crypto.transactions;

import org.bitcoin.protocols.payments.Protos;

import java.util.*;

public class TransactionAsset {
    public Signature signature = new Signature();
    public List<String> votes = new ArrayList<>();
    public Delegate delegate = new Delegate();
    public MultiSignature multisignature = new MultiSignature();
    public MultiPayment multiPayment = new MultiPayment();

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


}
