package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.transactions.Transaction
import java.nio.ByteBuffer

class MultiSignatureRegistration extends AbstractDeserializer {
    MultiSignatureRegistration(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction)
    }

    void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2 as int)

        this.transaction.asset.multisignature = [keysgroup: []]

        transaction.asset.multisignature.min = this.buffer.get() & 0xff
        int count = this.buffer.get() & 0xff
        transaction.asset.multisignature.lifetime = this.buffer.get() & 0xff

        for (int i = 0; i < count; i++) {
            def key = this.serialized.substring(assetOffset + 6 + i * 66, assetOffset + 6 + (i + 1) * 66)

            transaction.asset.multisignature.keysgroup.add(key)
        }

        this.transaction.parseSignatures(this.serialized, assetOffset + 6 + count * 66)
    }
}
