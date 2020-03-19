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

        byte[] common = serializeCommon();
        byte[] vendorField = serializeVendorField();

        byte[] typeBuffer = this.transaction.serialize();

        byte[] signatures = serializeSignatures(skipSignature, skipSecondSignature);

        ByteBuffer buffer =
                ByteBuffer.allocate(
                        common.length + vendorField.length + typeBuffer.length + signatures.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(common);
        buffer.put(vendorField);
        buffer.put(typeBuffer);
        buffer.put(signatures);

        return buffer.array();
    }

    private byte[] serializeCommon() {
        ByteBuffer buffer = ByteBuffer.allocate(58);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

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

        return buffer.array();
    }

    private byte[] serializeVendorField() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (this.transaction.hasVendorField()) {
            if (this.transaction.vendorField != null && !this.transaction.vendorField.equals("")) {
                int vendorFieldLength = this.transaction.vendorField.length();
                buffer = ByteBuffer.allocate(vendorFieldLength + 1);
                buffer.put((byte) vendorFieldLength);
                buffer.put(this.transaction.vendorField.getBytes());
            } else if (this.transaction.vendorFieldHex != null
                    && !this.transaction.vendorFieldHex.isEmpty()) {
                int vendorFieldHexLength = this.transaction.vendorFieldHex.length();
                buffer = ByteBuffer.allocate(vendorFieldHexLength + 1);
                buffer.put((byte) (vendorFieldHexLength / 2));
                buffer.put(Hex.decode(this.transaction.vendorFieldHex));
            } else {
                buffer.put((byte) 0x00);
            }
        } else {
            buffer.put((byte) 0x00);
        }

        return buffer.array();
    }

    private byte[] serializeSignatures(boolean skipSignature, boolean skipSecondSignature) {
        ByteBuffer buffer = ByteBuffer.allocate(144);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (!skipSignature && this.transaction.signature != null) {
            buffer.put(Hex.decode(this.transaction.signature));
        }

        if (!skipSecondSignature && this.transaction.secondSignature != null) {
            buffer.put(Hex.decode(this.transaction.secondSignature));
        }
        byte[] result = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(result);

        return result;
    }
}
