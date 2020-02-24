package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class MultiPayment extends Transaction {

    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.MULTI_PAYMENT.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public boolean hasVendorField() {
        return true;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(2 + this.asset.multiPayment.payments.size() * 29);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) this.asset.multiPayment.payments.size());
        for (TransactionAsset.Payment current : this.asset.multiPayment.payments) {
            buffer.putLong(current.amount);
            buffer.put(Base58.decodeChecked(current.recipientId));
        }
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        int paymentLength = buffer.getShort() & 0xff;
        for (int i = 0; i < paymentLength; i++) {
            byte[] recipientId = new byte[21];
            long amount = buffer.getLong();
            buffer.get(recipientId);
            this.asset.multiPayment.payments.add(
                    new TransactionAsset.Payment(amount, Base58.encodeChecked(recipientId)));
        }
    }
}
