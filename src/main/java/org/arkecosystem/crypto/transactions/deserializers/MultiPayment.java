package org.arkecosystem.crypto.transactions.deserializers;

import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class MultiPayment extends AbstractDeserializer {
    public MultiPayment(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset){
        this.buffer.position(assetOffset / 2);

        int paymentLength = this.buffer.get() & 0xff;

        for (int i = 0; i < paymentLength; i++) {
            byte[] recipientId = new byte[21];
            this.buffer.get(recipientId);
            this.transaction.asset.multiPayment.payments.put(Base58.encodeChecked(recipientId), this.buffer.getLong());
        }
    }

}
