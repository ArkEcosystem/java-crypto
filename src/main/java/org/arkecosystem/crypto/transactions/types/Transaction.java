package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.SignatureDecodeException;

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
    public long amount = 0L;
    public int expiration;
    public String recipientId;
    public String id;

    public void computeId(){
        this.id = this.getId();
    }

    public String getId() {
        return Hex.encode(Sha256Hash.hash(new Serializer(this).serialize(false, false)));
    }

    public Transaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        this.senderPublicKey = privateKey.getPublicKeyAsHex();
        this.signature =
                Hex.encode(
                        privateKey
                                .sign(Sha256Hash.of(Serializer.serialize(this, true, true)))
                                .encodeToDER());

        return this;
    }

    public Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        this.secondSignature =
                Hex.encode(
                        privateKey
                                .sign(Sha256Hash.of(Serializer.serialize(this, false, true)))
                                .encodeToDER());

        return this;
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

        byte[] signature = Hex.decode(this.signature);
        byte[] bytes = Serializer.serialize(this, true, true);

        try {
            return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey());
        } catch (SignatureDecodeException e) {
            return false;
        }
    }

    public boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(secondPublicKey));

        byte[] signature = Hex.decode(this.secondSignature);
        byte[] bytes = Serializer.serialize(this, false, true);

        try {
            return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey());
        } catch (SignatureDecodeException e) {
            return false;
        }
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

        if (this.vendorField != null && !this.vendorField.isEmpty()) {
            map.put("vendorField", this.vendorField);
        }

        if (this.expiration > 0) {
            map.put("expiration", this.expiration);
        }

        HashMap<String, Object> asset = new HashMap<>();
        if (this.type == CoreTransactionTypes.SECOND_SIGNATURE_REGISTRATION.getValue()) {
            HashMap<String, String> publicKey = new HashMap<>();
            publicKey.put("publicKey", this.asset.signature.publicKey);
            asset.put("signature", publicKey);
        } else if (this.type == CoreTransactionTypes.VOTE.getValue()) {
            asset.put("votes", this.asset.votes);
        } else if (this.type == CoreTransactionTypes.DELEGATE_REGISTRATION.getValue()) {
            HashMap<String, String> delegate = new HashMap<>();
            delegate.put("username", this.asset.delegate.username);
            asset.put("delegate", delegate);
        } else if (this.type == CoreTransactionTypes.MULTI_SIGNATURE_REGISTRATION.getValue()) {
            HashMap<String, Object> multisignature = new HashMap<>();
            multisignature.put("min", this.asset.multisignature.min);
            multisignature.put("lifetime", this.asset.multisignature.lifetime);
            multisignature.put("keysgroup", this.asset.multisignature.keysgroup);
            asset.put("multisignature", multisignature);
        } else if (this.type == CoreTransactionTypes.IPFS.getValue()) {
            asset.put("ipfs", this.asset.ipfs);
        } else if (this.type == CoreTransactionTypes.MULTI_PAYMENT.getValue()) {
            ArrayList<HashMap<String, String>> payments = new ArrayList<>();
            for (TransactionAsset.Payment current : this.asset.multiPayment.payments) {
                HashMap<String, String> payment = new HashMap<>();
                payment.put("amount", String.valueOf(current.amount));
                payment.put("recipientId", current.recipientId);
                payments.add(payment);
            }
            asset.put("payments", payments);
        }

        if (!asset.isEmpty()) {
            map.put("asset", asset);
        }
        return map;
    }

    public abstract byte[] serialize();

    public abstract void deserialize(ByteBuffer buffer);

    public abstract int getTransactionType();

    public abstract int getTransactionTypeGroup();

    public boolean hasVendorField() {
        return false;
    }
}
