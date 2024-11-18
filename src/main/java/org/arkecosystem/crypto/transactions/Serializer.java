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
        // Usamos un ByteBuffer dinámico
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.order(ByteOrder.BIG_ENDIAN);

        serializeCommon(buffer);
        serializeData(buffer);
        serializeSignatures(buffer, skipSignature);

        // Ajustamos el tamaño del buffer al tamaño real de los datos
        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);

        return result;
    }

    private void serializeCommon(ByteBuffer buffer) {
        // Escribimos el campo 'network' como UInt8
        buffer.put((byte) (transaction.network));

        // Escribimos 'nonce' como Uint64
        buffer.putLong(transaction.nonce);

        // Escribimos 'gasPrice' como Uint32
        buffer.putInt((int) transaction.gasPrice);

        // Escribimos 'gasLimit' como Uint32
        buffer.putInt((int) transaction.gasLimit);
    }

    private void serializeData(ByteBuffer buffer) {
        // Escribimos 'value' como Uint256 (32 bytes)
        byte[] valueBytes = BigInteger.valueOf(transaction.value).toByteArray();
        byte[] valueBytesPadded = new byte[32];
        int srcPos = Math.max(0, valueBytes.length - 32);
        int destPos = 32 - (valueBytes.length - srcPos);
        System.arraycopy(valueBytes, srcPos, valueBytesPadded, destPos, valueBytes.length - srcPos);
        buffer.put(valueBytesPadded);

        // Marcador de destinatario y dirección
        if (transaction.recipientAddress != null && !transaction.recipientAddress.isEmpty()) {
            buffer.put((byte) 1); // Marcador de destinatario presente
            // Convertimos la dirección del destinatario a bytes
            byte[] recipientBytes = Hex.decode(transaction.recipientAddress.replaceFirst("^0x", ""));
            buffer.put(recipientBytes);
        } else {
            buffer.put((byte) 0); // Marcador de destinatario ausente
        }

        // Escribimos la longitud del 'payload' como UInt32
        String payloadHex = transaction.data != null ? transaction.data.replaceFirst("^0x", "") : "";
        int payloadLength = payloadHex.length() / 2;
        buffer.putInt(payloadLength);

        // Escribimos el 'payload' como bytes
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
