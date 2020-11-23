package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.signature.ECDSAVerifier;
import org.arkecosystem.crypto.signature.SchnorrSigner;
import org.arkecosystem.crypto.signature.SchnorrVerifier;
import org.arkecosystem.crypto.signature.Signer;
import org.arkecosystem.crypto.signature.Verifier;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Transaction {

    public int version;
    public int network;
    public int typeGroup;
    public int type;
    public long nonce;
    public String senderPublicKey;
    public long fee = 0L;
    public String vendorField;
    public String vendorFieldHex;
    public TransactionAsset asset = new TransactionAsset();
    public String signature;
    public String secondSignature;
    public List<String> signatures;
    public long amount = 0L;
    public int expiration;
    public String recipientId;
    public String id;

    public void computeId() {
        this.id = this.getId();
    }

    public String getId() {
        return Hex.encode(Sha256Hash.hash(Serializer.serialize(this)));
    }

    public Transaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        this.senderPublicKey = privateKey.getPublicKeyAsHex();
        Sha256Hash hash = Sha256Hash.of(Serializer.serialize(this, true, true, false));

        this.signature = Hex.encode(signer().sign(hash.getBytes(), privateKey));

        return this;
    }

    public Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        Sha256Hash hash = Sha256Hash.of(Serializer.serialize(this, false, true, false));

        this.secondSignature = Hex.encode(signer().sign(hash.getBytes(), privateKey));

        return this;
    }

    public Transaction multiSign(String passphrase, int index) {
        if (this.signatures == null) {
            this.signatures = new ArrayList<>();
        }

        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        // This is needed given as no method senderPublicKey() is exposed in the builder
        if (this.senderPublicKey == null) {
            this.senderPublicKey = privateKey.getPublicKeyAsHex();
        }

        byte[] hash = Sha256Hash.hash(Serializer.serialize(this, true, true, true));
        String signature = Hex.encode(signer().sign(hash, privateKey));
        String indexedSignature = Hex.encode(new byte[]{(byte) index}) + signature;
        this.signatures.add(indexedSignature);

        return this;
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

        byte[] signature = Hex.decode(this.signature);
        byte[] hash = Sha256Hash.hash(Serializer.serialize(this, true, true, false));

        return verifier(this.signature).verify(hash, keys, signature);
    }

    public boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(secondPublicKey));

        byte[] signature = Hex.decode(this.secondSignature);
        byte[] hash = Sha256Hash.hash(Serializer.serialize(this, false, true, false));

        return verifier(this.secondSignature).verify(hash, keys, signature);
    }

    public boolean multiVerify(int min, List<String> publicKeys) {
        if (publicKeys.isEmpty()) {
            throw new RuntimeException("The multi signature asset is invalid.");
        }

        byte[] hash = Sha256Hash.hash(Serializer.serialize(this, true, true, true));

        Set<Integer> publicKeyIndexes = new HashSet<>();
        int verifiedSignatures = 0;
        boolean verified = false;
        for (int i = 0; i < this.signatures.size(); i++) {
            String signature = this.signatures.get(i);
            int publicKeyIndex = Integer.parseInt(signature.substring(0, 2), 16);

            if (!publicKeyIndexes.contains(publicKeyIndex)) {
                publicKeyIndexes.add(publicKeyIndex);
            } else {
                throw new RuntimeException("Duplicate participant in multi signature");
            }

            String partialSignature = signature.substring(2);
            String publicKey = publicKeys.get(publicKeyIndex);

            if (verifier(partialSignature).verify(hash, ECKey.fromPublicOnly(Hex.decode(publicKey)), Hex.decode(partialSignature))) {
                verifiedSignatures++;
            }

            if (verifiedSignatures == min) {
                verified = true;
                break;
            } else if (signatures.size() - (i + 1 - verifiedSignatures) < min) {
                break;
            }
        }
        return verified;
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create().toJson(this.toHashMap());
    }

    public HashMap toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("network", this.network);
        map.put("id", this.id);
        map.put("amount", String.valueOf(this.amount));
        map.put("fee", String.valueOf(this.fee));
        map.put("recipientId", this.recipientId);
        map.put("signature", this.signature);
        map.put("senderPublicKey", this.senderPublicKey);
        map.put("type", this.type);
        map.put("version", this.version);
        map.put("nonce", String.valueOf(this.nonce));
        map.put("typeGroup", this.typeGroup);

        if (this.secondSignature != null) {
            map.put("secondSignature", this.secondSignature);
        }

        if (this.signatures != null) {
            map.put("signatures", this.signatures);
        }

        if (this.vendorField != null && !this.vendorField.isEmpty()) {
            map.put("vendorField", this.vendorField);
        }

        if (this.expiration > 0) {
            map.put("expiration", this.expiration);
        }

        HashMap<String, Object> asset = this.assetToHashMap();
        if (asset != null && !asset.isEmpty()) {
            map.put("asset", asset);
        }
        return map;
    }

    public abstract byte[] serialize();

    public abstract void deserialize(ByteBuffer buffer);

    public abstract int getTransactionType();

    public abstract int getTransactionTypeGroup();

    public abstract HashMap<String, Object> assetToHashMap();

    public boolean hasVendorField() {
        return false;
    }

    private Signer signer() {
        return new SchnorrSigner();
    }

    private Verifier verifier(String signature) {
        // 128 string length => 64 bits
        return signature.length() == 128 ? new SchnorrVerifier() : new ECDSAVerifier();
    }
}
