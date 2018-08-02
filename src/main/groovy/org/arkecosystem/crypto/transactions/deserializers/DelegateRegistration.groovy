package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.transactions.Transaction
import org.arkecosystem.crypto.encoding.*
import java.nio.ByteBuffer

class DelegateRegistration extends AbstractDeserializer {
    DelegateRegistration(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction)
    }

    void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2 as int)

        int usernameLength = this.buffer.get() & 0xff

        byte[] username = new byte[usernameLength]
        this.buffer.get(username)

        transaction.asset.delegate = [username: new String(username)]

        this.transaction.parseSignatures(this.serialized, assetOffset + (usernameLength + 1) * 2)
    }
}
