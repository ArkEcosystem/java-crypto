package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class SecondSignatureRegistration extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.SECOND_SIGNATURE_REGISTRATION.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(Hex.decode(this.asset.signature.publicKey));
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        byte[] publicKeyBuffer = new byte[33];
        buffer.get(publicKeyBuffer);
        this.asset.signature.publicKey = Hex.encode(publicKeyBuffer);

        byte[] signatureBuffer = new byte[buffer.remaining()];
        buffer.get(signatureBuffer);
        this.signature = Hex.encode(signatureBuffer);
    }
}
