package org.arkecosystem.crypto.identities;

import java.util.Arrays;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public class PrivateKey {
    public static ECKey fromPassphrase(String passphrase) {
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes());

        return ECKey.fromPrivate(sha256, true);
    }

    public static ECKey fromHex(String privateKey) {
        return ECKey.fromPrivate(Hex.decode(privateKey), true);
    }

    public static ECKey fromWif(String wif) {
        byte[] decoded = Base58.decodeChecked(wif);

        if (decoded.length < 33) {
            throw new IllegalArgumentException("Invalid WIF: payload too short.");
        }

        int expectedVersion = Network.get().wif() & 0xff;
        if ((decoded[0] & 0xff) != expectedVersion) {
            throw new IllegalArgumentException(
                    "Invalid WIF: version byte does not match the active network.");
        }

        byte[] privateKeyBytes = Arrays.copyOfRange(decoded, 1, 33);

        return ECKey.fromPrivate(privateKeyBytes, true);
    }
}
