package org.arkecosystem.crypto.transactions;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;

public class Serializer {
    private final AbstractTransaction transaction;

    private Serializer(AbstractTransaction transaction) {
        this.transaction = transaction;
    }

    public static Serializer newSerializer(AbstractTransaction transaction) {
        return new Serializer(transaction);
    }

    public static byte[] getBytes(AbstractTransaction transaction, boolean skipSignature) {
        return new Serializer(transaction).serialize(skipSignature);
    }

    public byte[] serialize(boolean skipSignature) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        serializeCommon(buffer);
        serializeData(buffer);
        serializeSignatures(buffer, skipSignature);

        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        return result;
    }

    private void serializeCommon(ByteBuffer buffer) {
        buffer.put((byte) transaction.network);
        buffer.putLong(transaction.nonce);
        buffer.putInt((int) transaction.gasPrice);
        buffer.putInt((int) transaction.gasLimit);
    }

    private void serializeData(ByteBuffer buffer) {
        // Convert 'value' from String to BigInteger and write as Uint256 (32 bytes)
        byte[] valueBytes = new BigInteger(transaction.value).toByteArray();
        byte[] valueBytesPadded = new byte[32];
        int srcPos = Math.max(0, valueBytes.length - 32);
        int destPos = 32 - (valueBytes.length - srcPos);
        System.arraycopy(valueBytes, srcPos, valueBytesPadded, destPos, valueBytes.length - srcPos);
        buffer.put(valueBytesPadded);

        // Write recipient marker and address
        if (transaction.recipientAddress != null && !transaction.recipientAddress.isEmpty()) {
            buffer.put((byte) 1);
            byte[] recipientBytes =
                    Hex.decode(transaction.recipientAddress.replaceFirst("^0x", "").toLowerCase());

            buffer.put(recipientBytes);
        } else {
            buffer.put((byte) 0);
        }

        // Write payload length as UInt32 and the payload itself if present
        String payloadHex =
                transaction.data != null ? transaction.data.replaceFirst("^0x", "") : "";
        int payloadLength = payloadHex.length() / 2;
        buffer.putInt(payloadLength);
        if (payloadLength > 0) {
            buffer.put(Hex.decode(payloadHex));
        }
    }

    private void serializeSignatures(ByteBuffer buffer, boolean skipSignature) {
        if (!skipSignature && transaction.signature != null) {
            buffer.put(Hex.decode(transaction.signature));
        }
    }
}
