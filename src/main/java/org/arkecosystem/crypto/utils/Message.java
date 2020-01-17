package org.arkecosystem.crypto.utils;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.SignatureDecodeException;

public class Message {
    private String publickey;
    private String signature;
    private String message;

    public Message(String publickey, String signature, String message) {
        this.publickey = publickey;
        this.signature = signature;
        this.message = message;
    }

    public static Message sign(String message, String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);
        Sha256Hash messageBytes = Sha256Hash.of(message.getBytes());

        return new Message(
                privateKey.getPublicKeyAsHex(),
                Hex.encode(privateKey.sign(messageBytes).encodeToDER()),
                message);
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.publickey));

        byte[] signature = Hex.decode(this.signature);
        byte[] messageBytes = Sha256Hash.hash(this.message.getBytes());

        try {
            return ECKey.verify(messageBytes, signature, keys.getPubKey());
        } catch (SignatureDecodeException e) {
            return false;
        }
    }

    public Map toMap() {
        HashMap<String, Object> map = new HashMap();
        map.put("message", this.message);
        map.put("publickey", this.publickey);
        map.put("signature", this.signature);
        return map;
    }

    public String toJson() {
        return new Gson().toJson(toMap());
    }

    public String getMessage() {
        return message;
    }

    public String getPublickey() {
        return publickey;
    }

    public String getSignature() {
        return signature;
    }
}
