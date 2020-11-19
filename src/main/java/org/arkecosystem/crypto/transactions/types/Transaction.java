package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.signature.*;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Sha256Hash hash = Sha256Hash.of(Serializer.serialize(this, true, true, true));

        this.signature = Hex.encode(signer().sign(hash.getBytes(), privateKey));

        return this;
    }

    public Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        Sha256Hash hash = Sha256Hash.of(Serializer.serialize(this, false, true, true));

        this.secondSignature = Hex.encode(signer().sign(hash.getBytes(), privateKey));

        return this;
    }

    public Transaction multiSign(String passphrase) {
        return this.multiSign(passphrase, this.signatures.size());
    }

    public Transaction multiSign(String passphrase, int index) {
        if (this.signatures == null) {
            this.signatures = new ArrayList<>();
        }
        this.version = 2; // TODO Is this needed? I guess not. Yet, someone could first multiSign and then change the version manually. Even if we set it to 2 here.

        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        Sha256Hash hash = Sha256Hash.of(Serializer.serialize(this, true, true, true));

        String indexedSignature = Hex.encode(new byte[]{(byte) index}) + Hex.encode(signer().sign(hash.getBytes(), privateKey));
        this.signatures.add(indexedSignature);

        return this;
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

        byte[] signature = Hex.decode(this.signature);
        byte[] hash = Sha256Hash.hash(Serializer.serialize(this, true, true, true));

        return verifier(this.signature).verify(hash, keys, signature);
    }

    public boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(secondPublicKey));

        byte[] signature = Hex.decode(this.secondSignature);
        byte[] hash = Sha256Hash.hash(Serializer.serialize(this, false, true, true));

        return verifier(this.secondSignature).verify(hash, keys, signature);
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
