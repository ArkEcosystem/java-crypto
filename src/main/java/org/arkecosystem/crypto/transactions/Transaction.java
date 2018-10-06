package org.arkecosystem.crypto;

import com.google.gson.Gson;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.enums.Types;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Transaction {

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
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(getSenderPublicKey()));

        byte[] signature = Hex.decode(getSignature());
        byte[] bytes = toBytes();

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey());
    }

    public boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(secondPublicKey));

        byte[] signature = Hex.decode(this.getSignSignature());
        byte[] bytes = toBytes(false);

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey());
    }

    public Transaction parseSignatures(String serialized, int startOffset) {
        this.signature = serialized.substring(startOffset);

        Integer multiSignatureOffset = 0;

        if (this.signature.length() == 0) {
            this.signature = null;
        } else {
            Integer signatureLength = Integer.parseInt(this.signature.substring(2, 4), 16) + 2;
            this.signature = serialized.substring(startOffset, startOffset + signatureLength * 2);
            multiSignatureOffset += signatureLength * 2;
            this.secondSignature = serialized.substring(startOffset + signatureLength * 2);

            if (this.secondSignature.length() == 0) {
                this.secondSignature = null;
            } else {
                if ("ff".equals(this.secondSignature.substring(0, 2))) {
                    this.secondSignature = null;
                } else {
                    Integer secondSignatureLength = Integer.parseInt(this.secondSignature.substring(2, 4), 16) + 2;
                    this.secondSignature = this.secondSignature.substring(0, secondSignatureLength * 2);
                    multiSignatureOffset += secondSignatureLength * 2;
                }

            }


            String signatures = serialized.substring(startOffset + multiSignatureOffset);

            if (signatures.length() == 0) {
                return this;
            }


            if (signatures.substring(0, 2) != "ff") {
                return this;
            }

            signatures = signatures.substring(2);
            this.signatures = new ArrayList<>();

            Boolean moreSignatures = true;
            while (moreSignatures) {
                Integer mLength = Integer.parseInt(signatures.substring(2, 4), 16) + 2;

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

    public byte[] toBytes(boolean skipSignature, boolean skipSecondSignature) {
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte)type.getValue());
        buffer.putInt(timestamp);
        buffer.put(Hex.decode(getSenderPublicKey()));

        if (!recipientId.isEmpty()) {
            buffer.put(Base58.decodeChecked(getRecipientId()));
        } else {
            buffer.put(new byte[21]);
        }

        if (!vendorField.isEmpty()) {
            byte[] vbytes = vendorField.getBytes();
            if (vbytes.length < 65) {
                buffer.put(vbytes);
                buffer.put(new byte[64 - vbytes.length]);
            }

        } else {
            buffer.put(new byte[64]);
        }


        buffer.putLong(amount);
        buffer.putLong(fee);

        if (this.type == Types.TRANSFER) {
           // buffer.put(Hex.decode(getAsset().signature.publicKey));
        }


        if (this.type == Types.DELEGATE_REGISTRATION) {
            //buffer.put(getAsset().delegate.username.bytes);
        }


        if (this.type == Types.VOTE) {
            //buffer.put(getAsset().votes.join("").bytes);
        }


        if (this.type == Types.MULTI_SIGNATURE_REGISTRATION) {
            //buffer.put(getAsset().multisignature.min);
            //buffer.put(getAsset().multisignature.lifetime);
            //buffer.put(getAsset().multisignature.keysgroup.join"");
        }

        if (!skipSignature && signature != null) {
            buffer.put(Hex.decode(getSignature()));
        }


        if (!skipSecondSignature && signSignature != null) {
            buffer.put(Hex.decode(getSignSignature()));
        }

        byte[] result = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(result);
        return result;
    }

    public byte[] toBytes(boolean skipSignature) {
        return toBytes(skipSignature, true);
    }

    public byte[] toBytes() {
        return toBytes(true, true);
    }

    public String serialize() {
        return Hex.encode(new Serializer().serialize(this));
    }

    public static Transaction deserialize(String serialized) {
        return new Deserializer().deserialize(serialized);
    }

    public String toObject() {
     //   return DefaultGroovyMethods.subMap(DefaultGroovyMethods.getProperties(this), new ArrayList<String>(Arrays.asList("id", "timestamp", "recipientId", "amount", "fee", "type", "asset", "vendorField", "signature", "signSignature", "senderPublicKey")));
    }

    public String toJson() {
        return new Gson().toJson(toObject());
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public HashMap<String, Object> getAsset() {
    //    return asset;
    }

    public void setAsset(Map<String, Object> asset) {
        this.asset = asset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSecondSignature() {
        return secondSignature;
    }

    public void setSecondSignature(String secondSignature) {
        this.secondSignature = secondSignature;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignSignature() {
        return signSignature;
    }

    public void setSignSignature(String signSignature) {
        this.signSignature = signSignature;
    }

    public String getVendorField() {
        return vendorField;
    }

    public void setVendorField(String vendorField) {
        this.vendorField = vendorField;
    }

    public String getVendorFieldHex() {
        return vendorFieldHex;
    }

    public void setVendorFieldHex(String vendorFieldHex) {
        this.vendorFieldHex = vendorFieldHex;
    }

    private int expiration;
    private int network;
    private int timestamp;
    private Types type;
    private int version;
    private List<String> signatures;
    private Long amount = 0L;
    private Long fee = 0L;
    private Map<String, Object> asset = new HashMap<String, Object>();
    private String id;
    private String recipientId;
    private String secondSignature;
    private String senderPublicKey;
    private String signature;
    private String signSignature;
    private String vendorField;
    private String vendorFieldHex;
}
