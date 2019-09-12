package org.arkecosystem.crypto.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public class MultiPayment{
        public HashMap <String, Long> payments = new HashMap<>();
    }
}
