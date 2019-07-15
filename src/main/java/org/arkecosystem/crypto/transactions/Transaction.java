package org.arkecosystem.crypto.transactions;

import com.google.gson.*;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.Types;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Transaction {
    public int expiration;
    public int network;
    public int timestamp;
    public Types type;
    public int version;
    public List<String> signatures;
    public long amount = 0L;
    public long fee = 0L;
    public TransactionAsset asset = new TransactionAsset();
    public String id;
    public String recipientId;
    public String secondSignature;
    public String senderPublicKey;
    public String signature;
    public String signSignature;
    public String vendorField;

    public String vendorFieldHex;

    public static Transaction deserialize(String serialized) {
        return new Deserializer().deserialize(serialized);
    }

    public String computeId() {
        return Hex.encode(Sha256Hash.hash(toBytes(false, false)));
    }

    public Transaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        this.senderPublicKey = privateKey.getPublicKeyAsHex();
        this.signature = Hex.encode(privateKey.sign(Sha256Hash.of(toBytes())).encodeToDER());

        return this;
    }

    public Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        this.signSignature = Hex.encode(privateKey.sign(Sha256Hash.of(toBytes(false))).encodeToDER());

        return this;
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

        byte[] signature = Hex.decode(this.signature);
        byte[] bytes = toBytes();

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey());
    }

    public boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(secondPublicKey));

        byte[] signature = Hex.decode(this.signSignature);
        byte[] bytes = toBytes(false);

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey());
    }

    public Transaction parseSignatures(String serialized, int startOffset) {
        this.signature = serialized.substring(startOffset);

        int multiSignatureOffset = 0;

        if (this.signature.length() == 0) {
            this.signature = null;
        } else {
            int signatureLength = Integer.parseInt(this.signature.substring(2, 4), 16) + 2;
            this.signature = serialized.substring(startOffset, startOffset + signatureLength * 2);
            multiSignatureOffset += signatureLength * 2;
            this.secondSignature = serialized.substring(startOffset + signatureLength * 2);

            if (this.secondSignature.length() == 0) {
                this.secondSignature = null;
            } else {
                if ("ff".equals(this.secondSignature.substring(0, 2))) {
                    this.secondSignature = null;
                } else {
                    int secondSignatureLength = Integer.parseInt(this.secondSignature.substring(2, 4), 16) + 2;
                    this.secondSignature = this.secondSignature.substring(0, secondSignatureLength * 2);
                    multiSignatureOffset += secondSignatureLength * 2;
                }

            }

            String signatures = serialized.substring(startOffset + multiSignatureOffset);
            if (signatures.length() == 0) {
                return this;
            }

            if (!signatures.substring(0, 2).equals("ff")) {
                return this;
            }

            signatures = signatures.substring(2);
            this.signatures = new ArrayList<>();

            boolean moreSignatures = true;
            while (moreSignatures) {
                int mLength = Integer.parseInt(signatures.substring(2, 4), 16) + 2;

                if (mLength > 0) {
                    this.signatures.add(signatures.substring(0, mLength * 2));
                } else {
                    moreSignatures = false;
                }


                signatures = signatures.substring(mLength * 2);

                if (signatures.length() == 0) {
                    break;
                }

            }

        }

        return this;
    }

    private byte[] toBytes(boolean skipSignature, boolean skipSecondSignature) {
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte) type.getValue());
        buffer.putInt(timestamp);
        buffer.put(Hex.decode(this.senderPublicKey));

        boolean skipRecipientId = this.type == Types.SECOND_SIGNATURE_REGISTRATION || this.type == Types.MULTI_SIGNATURE_REGISTRATION;
        if (recipientId != null && !recipientId.isEmpty() && !skipRecipientId) {
            buffer.put(Base58.decodeChecked(this.recipientId));
        } else {
            buffer.put(new byte[21]);
        }

        if (vendorField != null && !recipientId.isEmpty()) {
            byte[] vbytes = vendorField.getBytes();
            if (vbytes.length <= 255) {
                buffer.put(vbytes);
                if (vbytes.length < 64) {
                    buffer.put(new byte[64 - vbytes.length]);
                }
            }
        } else {
            buffer.put(new byte[64]);
        }

        buffer.putLong(amount);
        buffer.putLong(fee);

        if (this.type == Types.SECOND_SIGNATURE_REGISTRATION) {
            buffer.put(Hex.decode(this.asset.signature.publicKey));
        }

        if (this.type == Types.DELEGATE_REGISTRATION) {
            buffer.put(this.asset.delegate.username.getBytes());
        }

        if (this.type == Types.VOTE) {
            buffer.put(String.join("", this.asset.votes).getBytes());
        }

        if (this.type == Types.MULTI_SIGNATURE_REGISTRATION) {
            buffer.put(this.asset.multisignature.min);
            buffer.put(this.asset.multisignature.lifetime);
            buffer.put(String.join("", this.asset.multisignature.keysgroup).getBytes());
        }

        if (!skipSignature && signature != null) {
            buffer.put(Hex.decode(this.signature));
        }

        if (!skipSecondSignature && signSignature != null) {
            buffer.put(Hex.decode(this.signSignature));
        }

        byte[] result = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(result);
        return result;
    }

    private byte[] toBytes(boolean skipSignature) {
        return toBytes(skipSignature, true);
    }

    private byte[] toBytes() {
        return toBytes(true, true);
    }

    public String serialize() {
        return Hex.encode(new Serializer().serialize(this));
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create().toJson(this.toHashMap());
    }

    public HashMap toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("network", this.network);
        map.put("id", this.id);
        map.put("timestamp", this.timestamp);
        map.put("expiration", this.expiration);
        map.put("type", this.type.getValue());
        map.put("amount", this.amount);
        map.put("fee", this.fee);
        map.put("recipientId", this.recipientId);
        map.put("signature", this.signature);
        map.put("senderPublicKey", this.senderPublicKey);

        if (this.vendorField != null && !this.vendorField.isEmpty()) {
            map.put("vendorField", this.vendorField);
        }

        if (this.signSignature!= null && !this.signSignature.isEmpty()) {
            map.put("signSignature", this.signSignature);
        }

        HashMap<String, Object> asset = new HashMap();
        if (this.type == Types.SECOND_SIGNATURE_REGISTRATION) {
            HashMap<String, String> publicKey = new HashMap();
            publicKey.put("publicKey", this.asset.signature.publicKey);
            asset.put("signature", publicKey);
        } else if (this.type == Types.VOTE) {
            asset.put("votes", this.asset.votes);
        } else if (this.type == Types.DELEGATE_REGISTRATION) {
            HashMap<String, String> delegate = new HashMap();
            delegate.put("username", this.asset.delegate.username);
            asset.put("delegate", delegate);
        } else if (this.type == Types.MULTI_SIGNATURE_REGISTRATION) {
            HashMap<String, Object> multisignature = new HashMap();
            multisignature.put("min", this.asset.multisignature.min);
            multisignature.put("lifetime", this.asset.multisignature.lifetime);
            multisignature.put("keysgroup", this.asset.multisignature.keysgroup);
            asset.put("multisignature", multisignature);
        }

        if (!asset.isEmpty()) {
            map.put("asset", asset);
        }
        return map;
    }

    private static class TransactionTypeDeserializer implements
        JsonDeserializer<Types> {
        @Override
        public Types deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Types.values()[json.getAsInt()];
        }
    }

    private static class TransactionTypeSerializer implements
        JsonSerializer<Types> {
        @Override
        public JsonElement serialize(Types src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getValue());
        }
    }
}
