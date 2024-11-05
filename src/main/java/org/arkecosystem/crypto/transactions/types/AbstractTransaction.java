package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.signature.SchnorrSigner;
import org.arkecosystem.crypto.signature.SchnorrVerifier;
import org.arkecosystem.crypto.signature.Signer;
import org.arkecosystem.crypto.signature.Verifier;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public abstract class AbstractTransaction {
    public int version;
    public int network;
    public int typeGroup;
    public int type;
    public long nonce;
    public String senderPublicKey;
    public long fee = 0L;
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
        return Hex.encode(Sha256Hash.hash(this.serialize()));
    }

    public AbstractTransaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        this.senderPublicKey = privateKey.getPublicKeyAsHex();
        Sha256Hash hash = Sha256Hash.of(this.serialize(true, true, false));

        this.signature = Hex.encode(signer().sign(hash.getBytes(), privateKey));

        return this;
    }

    public AbstractTransaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        Sha256Hash hash = Sha256Hash.of(this.serialize(false, true));

        this.secondSignature = Hex.encode(signer().sign(hash.getBytes(), privateKey));

        return this;
    }

    public AbstractTransaction multiSign(String passphrase, int index) {
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
        String indexedSignature = Hex.encode(new byte[] {(byte) index}) + signature;
        this.signatures.add(indexedSignature);

        return this;
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

        byte[] signature = Hex.decode(this.signature);
        byte[] hash = Sha256Hash.hash(this.serialize(true, true, false));

        return verifier().verify(hash, keys, signature);
    }

    public boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(secondPublicKey));

        byte[] signature = Hex.decode(this.secondSignature);
        byte[] hash = Sha256Hash.hash(this.serialize(false, true, false));

        return verifier().verify(hash, keys, signature);
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create().toJson(this.toHashMap());
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("fee", String.valueOf(this.fee));
        map.put("id", this.id);
        map.put("network", this.network);
        map.put("nonce", String.valueOf(this.nonce));
        map.put("senderPublicKey", this.senderPublicKey);
        map.put("signature", this.signature);
        map.put("type", this.type);
        map.put("typeGroup", this.typeGroup);
        map.put("version", this.version);
        map.put("recipientId", this.recipientId);
        map.put("amount", String.valueOf(this.amount));
        
        if (this.secondSignature != null) {
            map.put("secondSignature", this.secondSignature);
        }

        if (this.signatures != null) {
            map.put("signatures", this.signatures);
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

    public byte[] serialize(
            boolean skipSignature, boolean skipSecondSignature, boolean skipMultiSignature) {
        return Serializer.serialize(this, skipSignature, skipSecondSignature, skipMultiSignature);
    }

    public byte[] serialize(boolean skipSignature, boolean skipSecondSignature) {
        return serialize(skipSignature, skipSecondSignature, false);
    }

    public byte[] serialize(boolean skipSignature) {
        return serialize(skipSignature, false, false);
    }

    public byte[] serialize() {
        return serialize(false, false, false);
    }

    public abstract String getPayload();

    public abstract HashMap<String, Object> assetToHashMap();

    public List<Object> decodePayload(HashMap<String, Object> data) {
        if (!data.containsKey("asset") || !(data.get("asset") instanceof HashMap)) {
            return null;
        }

        HashMap<String, Object> asset = (HashMap<String, Object>) data.get("asset");
        if (!asset.containsKey("evmCall") || !(asset.get("evmCall") instanceof HashMap)) {
            return null;
        }

        HashMap<String, Object> evmCall = (HashMap<String, Object>) asset.get("evmCall");
        if (!evmCall.containsKey("payload") || evmCall.get("payload") == null) {
            return null;
        }

        String payload = (String) evmCall.get("payload");
        if (payload.isEmpty()) {
            return null;
        }

        try {
            AbiDecoder abiDecoder = new AbiDecoder();  // Instantiate AbiDecoder
            Map<String, Object> decodedData = abiDecoder.decodeFunctionData(payload);

            // Check if decodedData contains "args" and is a list
            if (decodedData.containsKey("args") && decodedData.get("args") instanceof List) {
                return (List<Object>) decodedData.get("args");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Signer signer() {
        return new SchnorrSigner();
    }

    private Verifier verifier() {
        return new SchnorrVerifier();
    }
}
