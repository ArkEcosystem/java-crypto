package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.Address;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.arkecosystem.crypto.utils.TransactionUtils;
import org.bitcoinj.core.ECKey;
import org.web3j.crypto.Hash;

public abstract class AbstractTransaction {
    // 0x8f ‖ "MAINSAIL_LSS_V1"
    private static final byte[] LEGACY_SECOND_SIGNATURE_DOMAIN =
            Hex.decode("8f4d41494e5341494c5f4c53535f5631");

    public int network;
    public long nonce;
    public String senderPublicKey;
    public String senderAddress;
    public String data;
    public long fee = 0L;
    public String signature;
    public String value = "0";
    public String recipientAddress;
    public String id;
    public long gasLimit;
    public long gasPrice;
    public String validatorPublicKey;
    public String validatorProof;
    public String vote;
    public List<String> multipaymentRecipients;
    public List<BigInteger> multipaymentAmounts;
    public String username;
    public String legacySecondSignature;

    public AbstractTransaction() {}

    public AbstractTransaction(Map<String, Object> data) {
        if (data.containsKey("network")) {
            this.network = ((Number) data.get("network")).intValue();
        }
        if (data.containsKey("nonce")) {
            Object nonceVal = data.get("nonce");
            if (nonceVal instanceof Number) {
                this.nonce = ((Number) nonceVal).longValue();
            } else {
                this.nonce = Long.parseLong(nonceVal.toString());
            }
        }
        if (data.containsKey("gasPrice")) {
            this.gasPrice = ((Number) data.get("gasPrice")).longValue();
        }
        if (data.containsKey("gasLimit")) {
            this.gasLimit = ((Number) data.get("gasLimit")).longValue();
        }
        if (data.containsKey("recipientAddress")) {
            this.recipientAddress = (String) data.get("recipientAddress");
        }
        if (data.containsKey("value")) {
            this.value = data.get("value").toString();
        }
        if (data.containsKey("data")) {
            this.data = (String) data.get("data");
        }
        if (data.containsKey("signature")) {
            this.signature = (String) data.get("signature");
        }
        if (data.containsKey("senderPublicKey")) {
            this.senderPublicKey = (String) data.get("senderPublicKey");
        }
        if (data.containsKey("id")) {
            this.id = (String) data.get("id");
        }
        if (data.containsKey("validatorPublicKey")) {
            this.validatorPublicKey = (String) data.get("validatorPublicKey");
        }
        if (data.containsKey("validatorProof")) {
            this.validatorProof = (String) data.get("validatorProof");
        }
        if (data.containsKey("vote")) {
            this.vote = (String) data.get("vote");
        }
        if (data.containsKey("senderAddress")) {
            this.senderAddress = (String) data.get("senderAddress");
        }
        if (data.containsKey("legacySecondSignature")) {
            this.legacySecondSignature = (String) data.get("legacySecondSignature");
        }
    }

    public String getPayload() {
        return this.data != null ? this.data : "";
    }

    public AbstractTransaction refreshPayloadData() {
        this.data = getPayload();
        return this;
    }

    public void computeId() {
        this.id = this.getId();
    }

    public String getId() {
        return Hex.encode(hash(false));
    }

    public byte[] hash(boolean skipSignature) {
        return TransactionUtils.toHash(toHashMap(), skipSignature);
    }

    public AbstractTransaction sign(String passphrase) {
        return sign(PrivateKey.fromPassphrase(passphrase));
    }

    public AbstractTransaction sign(ECKey privateKey) {
        this.senderPublicKey = privateKey.getPublicKeyAsHex();

        this.signature = signHash(this.hash(true), privateKey);

        return this;
    }

    public AbstractTransaction legacySecondSign(String secondPassphrase) {
        return legacySecondSign(PrivateKey.fromPassphrase(secondPassphrase));
    }

    public AbstractTransaction legacySecondSign(ECKey secondPrivateKey) {
        if (this.signature == null) {
            throw new IllegalStateException(
                    "Transaction must be signed before adding a legacy second signature");
        }

        byte[] transactionHash = this.hash(false);
        byte[] message = new byte[LEGACY_SECOND_SIGNATURE_DOMAIN.length + transactionHash.length];
        System.arraycopy(
                LEGACY_SECOND_SIGNATURE_DOMAIN,
                0,
                message,
                0,
                LEGACY_SECOND_SIGNATURE_DOMAIN.length);
        System.arraycopy(
                transactionHash,
                0,
                message,
                LEGACY_SECOND_SIGNATURE_DOMAIN.length,
                transactionHash.length);

        this.legacySecondSignature = signHash(Hash.sha3(message), secondPrivateKey);

        return this;
    }

    private static String signHash(byte[] hash, ECKey privateKey) {
        return Hex.encode(PrivateKey.sign(hash, privateKey));
    }

    public void recoverSender() {
        if (this.signature == null || this.signature.length() != 130) {
            throw new RuntimeException("Invalid signature");
        }

        ECKey recoveredKey = PublicKey.recover(this.hash(true), Hex.decode(this.signature));

        this.senderPublicKey = recoveredKey.getPublicKeyAsHex();

        this.senderAddress = Address.fromPublicKey(this.senderPublicKey);
    }

    public boolean verify() {
        try {
            if (this.senderPublicKey == null) {
                this.recoverSender();
            }

            ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));
            ECKey recoveredKey = PublicKey.recover(this.hash(true), Hex.decode(this.signature));

            return recoveredKey.getPubKeyPoint().equals(keys.getPubKeyPoint());
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] serialize() {
        return serialize(false);
    }

    public byte[] serialize(boolean skipSignature) {
        return Serializer.newSerializer(this).serialize(skipSignature);
    }

    public String toJson() {
        return new GsonBuilder().create().toJson(this.toHashMap());
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("gasPrice", this.gasPrice);
        map.put("network", this.network);
        map.put("id", this.id);
        map.put("gasLimit", this.gasLimit);
        map.put("nonce", this.nonce);
        map.put("senderPublicKey", this.senderPublicKey);
        map.put("senderAddress", this.senderAddress);
        map.put("signature", this.signature);
        map.put("recipientAddress", this.recipientAddress);
        map.put("value", this.value);
        map.put("data", this.data);
        map.put("legacySecondSignature", this.legacySecondSignature);
        return map;
    }

    public List<Object> decodePayload(Map<String, Object> data) {
        if (data == null || !data.containsKey("data")) return null;

        String payload = (String) data.get("data");
        if (payload == null || payload.isEmpty()) return null;

        try {
            AbiDecoder abiDecoder = new AbiDecoder();
            Map<String, Object> decodedData = abiDecoder.decodeFunctionData(payload);
            return (List<Object>) decodedData.get("args");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
