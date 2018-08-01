package org.arkecosystem.crypto.identities

class PublicKey {
    static String fromPassphrase(String passphrase) {
        return PrivateKey.fromPassphrase(passphrase).getPublicKeyAsHex()
    }
}
