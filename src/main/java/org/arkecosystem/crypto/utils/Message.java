package org.arkecosystem.crypto.utils;

import com.google.gson.Gson;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Message {
    public Message(String publickey, String signature, String message) {
        this.publickey = publickey;
        this.signature = signature;
        this.message = message;
    }

    public static Message sign(String message, String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);
        Sha256Hash messageBytes = Sha256Hash.of(message.getBytes());

        return new Message(privateKey.getPublicKeyAsHex(), Hex.encode(privateKey.sign(messageBytes).encodeToDER()), message);
    }

    public boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(Hex.decode(this.publickey));

        byte[] signature = Hex.decode(this.signature);
        byte[] messageBytes = Sha256Hash.hash(this.message.getBytes());

        return ECKey.verify(messageBytes, signature, keys.getPubKey());
    }

    public Map toMap() {
        return DefaultGroovyMethods.subMap(DefaultGroovyMethods.getProperties(this), new ArrayList<>(Arrays.asList("publickey", "signature", "message")));
    }

    public String toJson() {
        return new Gson().toJson(toMap());
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String publickey;
    private String signature;
    private String message;
}
