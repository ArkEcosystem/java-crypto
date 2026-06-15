package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.Address;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.arkecosystem.crypto.utils.TransactionUtils;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public abstract class AbstractTransaction {
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
        if (data.containsKey("vote")) {
            this.vote = (String) data.get("vote");
        }
        if (data.containsKey("senderAddress")) {
            this.senderAddress = (String) data.get("senderAddress");
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
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);
        this.senderPublicKey = privateKey.getPublicKeyAsHex();

        this.signature = signHash(this.hash(true), privateKey);

        return this;
    }

    public AbstractTransaction legacySecondSign(String secondPassphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(secondPassphrase);

        this.legacySecondSignature = signHash(this.hash(true), privateKey);

        return this;
    }

    private static String signHash(byte[] hash, ECKey privateKey) {
        ECKey.ECDSASignature signature = privateKey.sign(Sha256Hash.wrap(hash));

        int recId = -1;
        for (int i = 0; i < 4; i++) {
            ECKey k = ECKey.recoverFromSignature(i, signature, Sha256Hash.wrap(hash), true);
            if (k != null && k.getPubKeyPoint().equals(privateKey.getPubKeyPoint())) {
                recId = i;
                break;
            }
        }
        if (recId == -1) {
            throw new RuntimeException("Could not find recId");
        }

        byte[] rBytes = bigIntegerToBytes(signature.r, 32);
        byte[] sBytes = bigIntegerToBytes(signature.s, 32);

        byte[] signatureBytes = new byte[64];
        System.arraycopy(rBytes, 0, signatureBytes, 0, 32);
        System.arraycopy(sBytes, 0, signatureBytes, 32, 32);

        byte[] signatureWithRecId = new byte[65];
        System.arraycopy(signatureBytes, 0, signatureWithRecId, 0, 64);
        signatureWithRecId[64] = (byte) recId;

        return Hex.encode(signatureWithRecId);
    }

    private static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        byte[] src = b.toByteArray();
        byte[] dest = new byte[numBytes];
        int srcPos = Math.max(0, src.length - numBytes);
        int destPos = Math.max(0, numBytes - src.length);
        int length = Math.min(src.length, numBytes);
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    public void recoverSender() {
        if (this.signature == null || this.signature.length() != 130) {
            throw new RuntimeException("Invalid signature");
        }

        byte[] signatureWithRecId = Hex.decode(this.signature);
        if (signatureWithRecId.length != 65) {
            throw new RuntimeException("Invalid signature length");
        }

        byte recId = signatureWithRecId[64];
        byte[] signatureBytes = Arrays.copyOfRange(signatureWithRecId, 0, 64);

        BigInteger r = new BigInteger(1, Arrays.copyOfRange(signatureBytes, 0, 32));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(signatureBytes, 32, 64));

        ECKey.ECDSASignature signature = new ECKey.ECDSASignature(r, s);

        byte[] hash = this.hash(true);

        ECKey recoveredKey =
                ECKey.recoverFromSignature(recId, signature, Sha256Hash.wrap(hash), true);
        if (recoveredKey == null) {
            throw new RuntimeException("Could not recover public key from signature");
        }

        this.senderPublicKey = recoveredKey.getPublicKeyAsHex();

        this.senderAddress = Address.fromPublicKey(this.senderPublicKey);
    }

    public boolean verify() {
        try {
            if (this.senderPublicKey == null) {
                this.recoverSender();
            }

            ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

            byte[] signatureWithRecId = Hex.decode(this.signature);
            if (signatureWithRecId.length != 65) {
                return false;
            }

            byte recId = signatureWithRecId[64];
            byte[] signatureBytes = Arrays.copyOfRange(signatureWithRecId, 0, 64);

            BigInteger r = new BigInteger(1, Arrays.copyOfRange(signatureBytes, 0, 32));
            BigInteger s = new BigInteger(1, Arrays.copyOfRange(signatureBytes, 32, 64));

            ECKey.ECDSASignature signature = new ECKey.ECDSASignature(r, s);

            byte[] hash = this.hash(true);

            ECKey recoveredKey =
                    ECKey.recoverFromSignature(recId, signature, Sha256Hash.wrap(hash), true);
            if (recoveredKey == null) {
                return false;
            }

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
