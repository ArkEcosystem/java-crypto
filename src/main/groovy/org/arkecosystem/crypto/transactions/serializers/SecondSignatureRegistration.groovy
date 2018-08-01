package org.arkecosystem.crypto.transactions.serializers

import org.arkecosystem.crypto.transactions.Transaction
import java.nio.ByteBuffer
import static com.google.common.io.BaseEncoding.base16

class SecondSignatureRegistration extends AbstractSerializer {
    SecondSignatureRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction)
    }

    void serialize() {
        buffer.put base16().lowerCase().decode(this.transaction.asset.signature.publicKey)
    }
}
