package org.arkecosystem.crypto.transactions;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.transactions.types.*;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.bitcoinj.core.Sha256Hash;

public class Deserializer {
    private static final int SIGNATURE_SIZE = 64;
    private static final int RECOVERY_SIZE = 1;

    private final ByteBuffer buffer;

    public Deserializer(String serialized) {
        byte[] bytes = serialized.contains("\0") ? serialized.getBytes() : Hex.decode(serialized);
        this.buffer = ByteBuffer.wrap(bytes);
        this.buffer.order(ByteOrder.BIG_ENDIAN);
    }

    public static Deserializer newDeserializer(String serialized) {
        return new Deserializer(serialized);
    }

    public AbstractTransaction deserialize() {
        int startPosition = buffer.position();

        AbstractTransaction tempTransaction = new EvmCall();
        deserializeCommon(tempTransaction);
        deserializeData(tempTransaction);

        AbstractTransaction transaction = guessTransactionFromData(tempTransaction);

        buffer.position(startPosition);

        deserializeCommon(transaction);
        deserializeData(transaction);
        deserializeSignatures(transaction);

        // @TODO
        // transaction.recoverSender();
        transaction.id = Hex.encode(Sha256Hash.hash(transaction.hash(false)));

        return transaction;
    }

    private AbstractTransaction guessTransactionFromData(AbstractTransaction data) {
        if (data.value != "0") {
            return new Transfer();
        }

        Map<String, Object> payloadData = decodePayload(data);
        if (payloadData == null) {
            return new EvmCall();
        }

        String functionName = (String) payloadData.get("functionName");
        if (functionName.equals(AbiFunction.VOTE.toString())) {
            return new Vote();
        } else if (functionName.equals(AbiFunction.UNVOTE.toString())) {
            return new Unvote();
        } else if (functionName.equals(AbiFunction.VALIDATOR_REGISTRATION.toString())) {
            return new ValidatorRegistration();
        } else if (functionName.equals(AbiFunction.VALIDATOR_RESIGNATION.toString())) {
            return new ValidatorResignation();
        }

        return new EvmCall();
    }

    private Map<String, Object> decodePayload(AbstractTransaction transaction) {
        String payload = transaction.data != null ? transaction.data : "";
        if (payload.isEmpty()) {
            return null;
        }

        try {
            AbiDecoder abiDecoder = new AbiDecoder();
            return abiDecoder.decodeFunctionData(payload);
        } catch (Exception e) {
            return null;
        }
    }

    private void deserializeCommon(AbstractTransaction transaction) {
        transaction.network = Byte.toUnsignedInt(buffer.get());
        transaction.nonce = buffer.getLong();
        transaction.gasPrice = buffer.getInt();
        transaction.gasLimit = buffer.getInt();
        transaction.value = "0";
    }

    private void deserializeData(AbstractTransaction transaction) {
        byte[] valueBytes = new byte[32];
        buffer.get(valueBytes);
        String value = new BigInteger(1, valueBytes).toString();
        transaction.value = value;

        int recipientMarker = Byte.toUnsignedInt(buffer.get());
        if (recipientMarker == 1) {
            byte[] recipientBytes = new byte[20];
            buffer.get(recipientBytes);
            transaction.recipientAddress = "0x" + Hex.encode(recipientBytes);
        }

        int payloadLength = buffer.getInt();
        byte[] payloadBytes = new byte[payloadLength];
        buffer.get(payloadBytes);
        transaction.data = Hex.encode(payloadBytes);
    }

    private void deserializeSignatures(AbstractTransaction transaction) {
        int signatureLength = SIGNATURE_SIZE + RECOVERY_SIZE;
        if (buffer.remaining() >= signatureLength) {
            byte[] signatureBytes = new byte[signatureLength];
            buffer.get(signatureBytes);
            transaction.signature = Hex.encode(signatureBytes);
        }
    }
}
