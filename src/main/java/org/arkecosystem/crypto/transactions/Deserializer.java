package org.arkecosystem.crypto.transactions;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.types.DelegateRegistration;
import org.arkecosystem.crypto.transactions.types.DelegateResignation;
import org.arkecosystem.crypto.transactions.types.HtlcClaim;
import org.arkecosystem.crypto.transactions.types.HtlcLock;
import org.arkecosystem.crypto.transactions.types.HtlcRefund;
import org.arkecosystem.crypto.transactions.types.Ipfs;
import org.arkecosystem.crypto.transactions.types.MultiPayment;
import org.arkecosystem.crypto.transactions.types.MultiSignatureRegistration;
import org.arkecosystem.crypto.transactions.types.SecondSignatureRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.Transfer;
import org.arkecosystem.crypto.transactions.types.Vote;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Deserializer {

    private final ByteBuffer buffer;
    private Transaction transaction;

    private final Map<Integer, Map<Integer, Transaction>> transactionGroups = new HashMap<>();

    public Deserializer(String serialized) {
        Map<Integer, Transaction> coreTransactionTypes = new HashMap<>();
        coreTransactionTypes.put(CoreTransactionTypes.TRANSFER.getValue(), new Transfer());
        coreTransactionTypes.put(
            CoreTransactionTypes.SECOND_SIGNATURE_REGISTRATION.getValue(),
            new SecondSignatureRegistration());
        coreTransactionTypes.put(
            CoreTransactionTypes.DELEGATE_REGISTRATION.getValue(), new DelegateRegistration());
        coreTransactionTypes.put(CoreTransactionTypes.VOTE.getValue(), new Vote());
        coreTransactionTypes.put(
            CoreTransactionTypes.MULTI_SIGNATURE_REGISTRATION.getValue(),
            new MultiSignatureRegistration());
        coreTransactionTypes.put(CoreTransactionTypes.IPFS.getValue(), new Ipfs());
        coreTransactionTypes.put(CoreTransactionTypes.MULTI_PAYMENT.getValue(), new MultiPayment());
        coreTransactionTypes.put(
            CoreTransactionTypes.DELEGATE_RESIGNATION.getValue(), new DelegateResignation());
        coreTransactionTypes.put(CoreTransactionTypes.HTLC_LOCK.getValue(), new HtlcLock());
        coreTransactionTypes.put(CoreTransactionTypes.HTLC_CLAIM.getValue(), new HtlcClaim());
        coreTransactionTypes.put(CoreTransactionTypes.HTLC_REFUND.getValue(), new HtlcRefund());

        transactionGroups.put(TransactionTypeGroup.CORE.getValue(), coreTransactionTypes);

        this.buffer = ByteBuffer.wrap(Hex.decode(serialized)).slice();
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Transaction deserialize() {
        this.buffer.get();

        deserializeCommon();
        deserializeVendorField();

        this.transaction.deserialize(this.buffer);

        deserializeSignatures();

        this.transaction.computeId();

        return this.transaction;
    }

    private void deserializeCommon() {
        int version = this.buffer.get();
        int network = this.buffer.get();
        int typeGroup = this.buffer.getInt();
        int type = this.buffer.getShort();
        long nonce = this.buffer.getLong();

        this.transaction = this.transactionGroups.get(typeGroup).get(type);
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
        int vendorFieldLength = this.buffer.get();
        if (vendorFieldLength > 0) {
            byte[] vendorField = new byte[vendorFieldLength];
            this.buffer.get(vendorField);
            transaction.vendorField = new String(vendorField);
        }
    }

    private void deserializeSignatures() {
        deserializeSchnorrOrEcdsa();
    }

    private void deserializeSchnorrOrEcdsa() {
        if (detectSchnorr()) {
            deserializeSchnorr();
        } else {
            deserializeEcdsa();
        }
    }

    private void deserializeEcdsa() {
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

    private boolean canReadNonMultiSignature() {
        return buffer.hasRemaining() && (buffer.remaining() % 64 == 0 || buffer.remaining() % 65 != 0);
    }

    private void deserializeSchnorr() {
        if (canReadNonMultiSignature()) {
            byte[] signatureBuffer = new byte[64];
            buffer.get(signatureBuffer);
            transaction.signature = Hex.encode(signatureBuffer);
        }

        if (canReadNonMultiSignature()) {
            byte[] signatureBuffer = new byte[64];
            buffer.get(signatureBuffer);
            transaction.secondSignature = Hex.encode(signatureBuffer);
        }

        if (buffer.hasRemaining()) {
            if (buffer.remaining() % 65 == 0) {
                transaction.signatures = new ArrayList<>();

                int count = buffer.remaining() / 65;
                Set<Integer> publicKeyIndexes = new HashSet<>();
                for (int i = 0; i < count; i++) {
                    byte[] signatureBuffer = new byte[65];
                    buffer.get(signatureBuffer);
                    String multiSignaturePart = Hex.encode(signatureBuffer);
                    int publicKeyIndex = Integer.parseInt(multiSignaturePart.substring(0, 2), 16);

                    if (!publicKeyIndexes.contains(publicKeyIndex)) {
                        publicKeyIndexes.add(publicKeyIndex);
                    } else {
                        throw new RuntimeException("Duplicate participant in multi signature");
                    }

                    transaction.signatures.add(multiSignaturePart);
                }
            } else {
                throw new RuntimeException("signature buffer not exhausted");
            }
        }
    }

    private boolean detectSchnorr() {
        int remaining = buffer.remaining();

        // `signature` / `secondSignature`
        if (remaining == 64 || remaining == 128) {
            return true;
        }

        // `signatures` of a multi signature transaction (type != 4)
        if (remaining % 65 == 0) {
            return true;
        }

        // only possiblity left is a type 4 transaction with and without a `secondSignature`.
        if ((remaining - 64) % 65 == 0 || (remaining - 128) % 65 == 0) {
            return true;
        }

        return false;
    }

    private int currentSignatureLength() {
        int mark = this.buffer.position();
        this.buffer.position(mark + 1);
        String length = String.valueOf(this.buffer.get());
        int signatureLength = Integer.parseInt(length) + 2;
        this.buffer.position(mark);
        return signatureLength;
    }

    public void setNewTransactionType(Transaction transaction) {
        if (this.transactionGroups.containsKey(transaction.getTransactionTypeGroup())) {
            this.transactionGroups
                .get(transaction.getTransactionTypeGroup())
                .put(transaction.getTransactionType(), transaction);
        } else {
            Map<Integer, Transaction> newTransactionGroup = new HashMap<>();
            newTransactionGroup.put(transaction.getTransactionType(), transaction);
            this.transactionGroups.put(transaction.getTransactionTypeGroup(), newTransactionGroup);
        }
    }

    public boolean hasTransactionType(int typeGroup, int type) {
        if (!this.transactionGroups.containsKey(typeGroup)) {
            return false;
        }
        return this.transactionGroups.get(typeGroup).containsKey(type);
    }
}
