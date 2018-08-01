package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.transactions.Transaction
import java.nio.ByteBuffer

class SecondSignatureRegistration extends AbstractDeserializer {
    SecondSignatureRegistration(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction)
    }

    void deserialize(int assetOffset) {
        this.transaction.asset.signature = [
            publicKey: this.serialized.substring(assetOffset, assetOffset + 66)
        ]

        this.transaction.parseSignatures(this.serialized, assetOffset + 66)
    }
}
