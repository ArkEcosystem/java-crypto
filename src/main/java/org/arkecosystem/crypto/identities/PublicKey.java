package org.arkecosystem.crypto.identities;

import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;

public class PublicKey {
    public static String fromPassphrase(String passphrase) {
        return PrivateKey.fromPassphrase(passphrase).getPublicKeyAsHex();
    }

    public static ECKey fromHex(String publicKey) {
        ECKey key = ECKey.fromPublicOnly(Hex.decode(publicKey));
        return ECKey.fromPublicOnly(key.getPubKeyPoint().getEncoded(true));
    }
}
