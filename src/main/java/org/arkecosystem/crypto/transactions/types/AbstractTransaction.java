package org.arkecosystem.crypto.transactions.types;

import com.google.gson.GsonBuilder;

// import jnr.ffi.Struct.pid_t;

import org.arkecosystem.crypto.encoding.Hex;
// import org.arkecosystem.crypto.identities.Address;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.signature.ECDSAVerifier;
import org.arkecosystem.crypto.signature.ECDSASigner;
import org.arkecosystem.crypto.signature.Signer;
import org.arkecosystem.crypto.signature.Verifier;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.arkecosystem.crypto.utils.TransactionHasher;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
// import org.bitcoinj.crypto.ECKey.ECDSASignature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTransaction {
    public int network;
    public long nonce;
    public String senderPublicKey;
    public String data;
    public long fee = 0L;
    public String signature;
    public long value = 0L;
    public String recipientAddress;
    public String id;
    public int gasLimit;
    public int gasPrice;
    public String validatorPublicKey;
    public String vote;

    public AbstractTransaction() {
        this.data = "";
    }

    public String getPayload() {
        return this.data != null ? this.data : "";
    }

    public AbstractTransaction refreshPayloadData() {
        this.data = getPayload().replaceFirst("^0x", "");
        return this;
    }

    public void computeId() {
        this.id = this.getId();
    }

    public String getId() {
        return Hex.encode(hash(false));
    }

    public byte[] hash(boolean skipSignature) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("gasPrice", this.gasPrice);
        map.put("network", this.network);
        map.put("nonce", this.nonce);
        map.put("value", this.value);
        map.put("gasLimit", this.gasLimit);
        map.put("data", this.data);
        map.put("recipientAddress", this.recipientAddress);
        map.put("signature", this.signature);

        return TransactionHasher.toHash(map, true);
    }

    public AbstractTransaction sign(String passphrase) {
        byte[] bytes = this.hash(true);
        
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);

        

        // this.senderPublicKey = privateKey.getPublicKeyAsHex();
        // Sha256Hash hash = Sha256Hash.of(this.serialize(true, true, false));

        // @TODO: update this
        this.signature = getId();

        return this;
    }



    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.senderPublicKey));

        byte[] signatureBytes = Hex.decode(this.signature);
        // @todo checke if skipSignature is true or false
        byte[] hash = Sha256Hash.hash(this.serialize(true));

        return verifier().verify(hash, keys, signatureBytes);
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
    

    private Signer signer() {
        return new ECDSASigner();
    }

    private Verifier verifier() {
        return new ECDSAVerifier();
    }
}
