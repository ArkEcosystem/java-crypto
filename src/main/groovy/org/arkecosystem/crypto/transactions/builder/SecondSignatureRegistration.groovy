package org.arkecosystem.crypto.transactions.builder

import static org.arkecosystem.crypto.enums.Types.*
import org.arkecosystem.crypto.identities.PublicKey
import static com.google.common.io.BaseEncoding.base16

class SecondSignatureRegistration extends AbstractTransaction {
    AbstractTransaction signature(String signature)
    {
        this.transaction.asset.signature = [
            publicKey: base16().lowerCase().encode(PublicKey.fromPassphrase(signature).getBytes())
        ]

        return this
    }

    int getType()
    {
        SECOND_SIGNATURE_REGISTRATION.getValue()
    }
}
