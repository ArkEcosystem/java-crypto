package org.arkecosystem.crypto.transactions;

import java.math.BigInteger;
import java.util.List;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.types.*;
import org.arkecosystem.crypto.utils.RlpDecoder;
import org.arkecosystem.crypto.utils.TransactionTypeIdentifier;

public class Deserializer {

    private final byte[] rawBytes;

    public Deserializer(String serialized) {
        this.rawBytes = Hex.decode(serialized);
    }

    public static Deserializer newDeserializer(String serialized) {
        return new Deserializer(serialized);
    }

    public AbstractTransaction deserialize() {
        List<byte[]> fields = RlpDecoder.decode(rawBytes);

        // Fields: [nonce, gasPrice, gasLimit, to, value, data, v, r, s]
        long nonce = bytesToLong(fields.get(0));
        long gasPrice = bytesToLong(fields.get(1));
        long gasLimit = bytesToLong(fields.get(2));
        String recipientAddress = fields.get(3).length > 0 ? "0x" + Hex.encode(fields.get(3)) : "";
        String value = fields.get(4).length > 0 ? new BigInteger(1, fields.get(4)).toString() : "0";
        String data = fields.get(5).length > 0 ? Hex.encode(fields.get(5)) : "";

        // Recover signature
        String signature = null;
        if (fields.size() >= 9) {
            int vEncoded =
                    fields.get(6).length > 0 ? new BigInteger(1, fields.get(6)).intValue() : 0;
            byte[] r = fields.get(7);
            byte[] s = fields.get(8);

            int chainId = Network.get().chainId();
            int v = vEncoded - (chainId * 2 + 35);

            if (r.length > 0 || s.length > 0) {
                byte[] rPadded = padTo32(r);
                byte[] sPadded = padTo32(s);
                byte[] sigBytes = new byte[65];
                System.arraycopy(rPadded, 0, sigBytes, 0, 32);
                System.arraycopy(sPadded, 0, sigBytes, 32, 32);
                sigBytes[64] = (byte) v;
                signature = Hex.encode(sigBytes);
            }
        }

        // Create temp transaction to guess type
        AbstractTransaction tempTransaction = new EvmCall();
        tempTransaction.nonce = nonce;
        tempTransaction.gasPrice = gasPrice;
        tempTransaction.gasLimit = gasLimit;
        tempTransaction.recipientAddress = recipientAddress;
        tempTransaction.value = value;
        tempTransaction.data = data;
        tempTransaction.network = Network.get().version();

        AbstractTransaction transaction = guessTransactionFromTransactionData(tempTransaction);
        transaction.nonce = nonce;
        transaction.gasPrice = gasPrice;
        transaction.gasLimit = gasLimit;
        transaction.recipientAddress = recipientAddress;
        transaction.value = value;
        transaction.data = data;
        transaction.network = Network.get().version();
        transaction.signature = signature;

        if (signature != null) {
            transaction.recoverSender();
        }

        transaction.computeId();

        return transaction;
    }

    private AbstractTransaction guessTransactionFromTransactionData(
            AbstractTransaction transactionData) {
        String payload = transactionData.data != null ? transactionData.data : "";

        if (TransactionTypeIdentifier.isMultiPayment(payload)) {
            return new Multipayment(transactionData.toHashMap());
        }

        if (!"0".equals(transactionData.value) && !"".equals(transactionData.value)) {
            return new Transfer();
        }

        if (payload.isEmpty()) {
            return new EvmCall();
        }

        if (TransactionTypeIdentifier.isVote(payload)) {
            return new Vote(transactionData.toHashMap());
        } else if (TransactionTypeIdentifier.isUnvote(payload)) {
            return new Unvote(transactionData.toHashMap());
        } else if (TransactionTypeIdentifier.isValidatorRegistration(payload)) {
            return new ValidatorRegistration(transactionData.toHashMap());
        } else if (TransactionTypeIdentifier.isValidatorResignation(payload)) {
            return new ValidatorResignation(transactionData.toHashMap());
        } else if (TransactionTypeIdentifier.isUsernameRegistration(payload)) {
            return new UsernameRegistration(transactionData.toHashMap());
        } else if (TransactionTypeIdentifier.isUsernameResignation(payload)) {
            return new UsernameResignation(transactionData.toHashMap());
        }

        return new EvmCall();
    }

    private static long bytesToLong(byte[] bytes) {
        if (bytes.length == 0) return 0;
        return new BigInteger(1, bytes).longValue();
    }

    private static byte[] padTo32(byte[] input) {
        if (input.length >= 32) return input;
        byte[] padded = new byte[32];
        System.arraycopy(input, 0, padded, 32 - input.length, input.length);
        return padded;
    }
}
