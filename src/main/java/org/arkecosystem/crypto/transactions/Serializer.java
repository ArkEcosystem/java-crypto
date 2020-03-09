package org.arkecosystem.crypto.transactions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class Serializer {

    private Transaction transaction;

    public Serializer(Transaction transaction) {
        this.transaction = transaction;
    }

    public static byte[] serialize(Transaction transaction) {
        return new Serializer(transaction).serialize(false, false);
    }

    public static byte[] serialize(
            Transaction transaction, boolean skipSignature, boolean skipSecondSignature) {
        return new Serializer(transaction).serialize(skipSignature, skipSecondSignature);
    }

    public byte[] serialize(boolean skipSignature, boolean skipSecondSignature) {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        serializeCommon(buffer);
        serializeVendorField(buffer);

        byte[] typeBuffer = this.transaction.serialize();
        buffer.put(typeBuffer);

        serializeSignatures(buffer, skipSignature, skipSecondSignature);

        byte[] result = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(result);

        return result;
    }

    private void serializeCommon(ByteBuffer buffer) {
        buffer.put((byte) 0xff);
        if (this.transaction.version > 0) {
            buffer.put((byte) this.transaction.version);
        } else {
            buffer.put((byte) 0x01);
        }
        if (this.transaction.network > 0) {
            buffer.put((byte) this.transaction.network);
        } else {
            buffer.put((byte) Network.get().version());
        }

        buffer.putInt(this.transaction.typeGroup);
        buffer.putShort((short) this.transaction.type);
        buffer.putLong(this.transaction.nonce);

        buffer.put(Hex.decode(this.transaction.senderPublicKey));
        buffer.putLong(this.transaction.fee);
    }

    private void serializeVendorField(ByteBuffer buffer) {
        if (this.transaction.hasVendorField()) {
            if (this.transaction.vendorField != null && !this.transaction.vendorField.equals("")) {
                int vendorFieldLength = this.transaction.vendorField.length();
                buffer.put((byte) vendorFieldLength);
                buffer.put(this.transaction.vendorField.getBytes());
            } else if (this.transaction.vendorFieldHex != null
                    && !this.transaction.vendorFieldHex.equals("")) {
                int vendorFieldHexLength = this.transaction.vendorFieldHex.length();
                buffer.put((byte) (vendorFieldHexLength / 2));
                buffer.put(Hex.decode(this.transaction.vendorFieldHex));
            } else {
                buffer.put((byte) 0x00);
            }
        } else {
            buffer.put((byte) 0x00);
        }
    }

    private void serializeSignatures(
            ByteBuffer buffer, boolean skipSignature, boolean skipSecondSignature) {

        if (!skipSignature && this.transaction.signature != null) {
            buffer.put(Hex.decode(this.transaction.signature));
        }

        if (!skipSecondSignature && this.transaction.secondSignature != null) {
            buffer.put(Hex.decode(this.transaction.secondSignature));
        }
    }
}
