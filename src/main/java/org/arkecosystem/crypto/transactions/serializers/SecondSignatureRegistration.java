package org.arkecosystem.crypto.transactions.serializers


import java.nio.ByteBuffer
import org.arkecosystem.crypto.encoding.*

class SecondSignatureRegistration extends AbstractSerializer {
    SecondSignatureRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction)
    }

    void serialize() {
        buffer.put Hex.decode(this.transaction.asset.signature.publicKey)
    }
}
