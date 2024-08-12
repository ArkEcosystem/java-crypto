package org.arkecosystem.crypto.signature;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.secp256k1.api.P256K1XOnlyPubKey;
import org.bitcoinj.secp256k1.api.P256k1PubKey;
import org.bitcoinj.secp256k1.api.Secp256k1;

public class SchnorrVerifier implements Verifier {
    @Override
    public boolean verify(byte[] hash, ECKey keys, byte[] signature) {
        byte[] pubKey = keys.getPubKey();

        try (Secp256k1 secp = Secp256k1.get()) {
            P256k1PubKey pubkey = secp.ecPubKeyParse(new CompressedPubKeyDataImpl(pubKey)).get();

            P256K1XOnlyPubKey xOnly = pubkey.getXOnly();

            P256K1XOnlyPubKey xOnly2 = P256K1XOnlyPubKey.parse(xOnly.getSerialized()).get();

            return secp.schnorrSigVerify(signature, hash, xOnly2).get();
        }
    }
}
