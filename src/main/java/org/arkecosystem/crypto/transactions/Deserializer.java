package org.arkecosystem.crypto.transactions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.Transfer;
import org.arkecosystem.crypto.transactions.types.Vote;

public class Deserializer {

    private ByteBuffer buffer;
    private Transaction transaction;

    private Map<Integer, Transaction> transactionsClasses = new HashMap<>();

    public Deserializer(String serialized) {
        this.transactionsClasses.put(CoreTransactionTypes.TRANSFER.getValue(), new Transfer());
        this.transactionsClasses.put(CoreTransactionTypes.VOTE.getValue(), new Vote());

        this.buffer = ByteBuffer.wrap(Hex.decode(serialized)).slice();
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Transaction deserialize() {
        this.buffer.get();

        deserializeCommon();
        deserializeVendorField();

        this.transaction.deserialize(this.buffer);

        deserializeSignature();

        this.transaction.computeId();

        return this.transaction;
    }

    private void deserializeCommon() {
        int version = this.buffer.get();
        int network = this.buffer.get();
        int typeGroup = this.buffer.getInt();
        int type = this.buffer.getShort();
        long nonce = this.buffer.getLong();

        this.transaction = this.transactionsClasses.get(type);
        this.transaction.version = version;
        this.transaction.network = network;
        this.transaction.typeGroup = typeGroup;
        this.transaction.type = type;
        this.transaction.nonce = nonce;

        byte[] senderPublicKey = new byte[33];
        this.buffer.get(senderPublicKey);
        this.transaction.senderPublicKey = Hex.encode(senderPublicKey);

        this.transaction.fee = this.buffer.getLong();
    }

    private void deserializeVendorField() {
        // TODO check if deser is correct
        int vendorFieldLength = this.buffer.get();
        if (vendorFieldLength > 0) {
            byte[] vendorFieldHex = new byte[vendorFieldLength];
            this.buffer.get(vendorFieldHex);
            transaction.vendorFieldHex = Hex.encode(vendorFieldHex);
        }
    }

    private void deserializeSignature() {
        if (buffer.remaining() != 0) {
            int signatureLength = currentSignatureLength();
            byte[] signatureBuffer = new byte[signatureLength];
            this.buffer.get(signatureBuffer);
            this.transaction.signature = Hex.encode(signatureBuffer);
        }

        if (buffer.remaining() != 0) {
            int signatureLength = currentSignatureLength();
            byte[] signatureBuffer = new byte[signatureLength];
            this.buffer.get(signatureBuffer);
            this.transaction.secondSignature = Hex.encode(signatureBuffer);
        }
    }

    private int currentSignatureLength() {
        int mark = this.buffer.position();
        this.buffer.position(mark + 1);
        String length = String.valueOf(this.buffer.get());
        int signatureLength = Integer.parseInt(length) + 2;
        this.buffer.position(mark);
        return signatureLength;
    }
}
