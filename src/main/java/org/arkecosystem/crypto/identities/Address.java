package org.arkecosystem.crypto.identities;

import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;

public class Address {
    public static String fromPassphrase(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);
        return fromPrivateKey(privateKey);
    }

    public static String fromPublicKey(String publicKey) {
        byte[] publicKeyBytes = Hex.decode(publicKey);

        // Ensure the public key is uncompressed
        ECKey ecKey = ECKey.fromPublicOnly(publicKeyBytes);
        byte[] uncompressedPublicKeyBytes = ecKey.getPubKeyPoint().getEncoded(false);

        // Remove the prefix (0x04)
        byte[] rawPublicKey = new byte[uncompressedPublicKeyBytes.length - 1];
        System.arraycopy(uncompressedPublicKeyBytes, 1, rawPublicKey, 0, rawPublicKey.length);

        // Hash the public key using Keccak-256
        byte[] keccakHash = Hash.sha3(rawPublicKey);

        // Take the last 20 bytes of the Keccak-256 hash
        byte[] addressBytes = new byte[20];

        System.arraycopy(keccakHash, keccakHash.length - 20, addressBytes, 0, 20);

        // Convert to checksum address
        String address = "0x" + Hex.encode(addressBytes);

        return Keys.toChecksumAddress(address);
    }

    public static String fromPrivateKey(ECKey privateKey) {
        byte[] publicKeyBytes = privateKey.getPubKey();
        return fromPublicKey(Hex.encode(publicKeyBytes));
    }

    public static boolean validate(String address) {
        return address != null && address.matches("^0x[a-fA-F0-9]{40}$");
    }
}
