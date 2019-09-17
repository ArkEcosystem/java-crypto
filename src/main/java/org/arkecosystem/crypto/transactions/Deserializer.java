package org.arkecosystem.crypto.transactions;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.identities.Address;
import org.arkecosystem.crypto.transactions.deserializers.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Deserializer {

    private String serialized;
    private ByteBuffer buffer;
    private Transaction transaction;

    public Transaction deserialize(String serialized) {
        this.serialized = serialized;
        this.buffer = ByteBuffer.wrap(Hex.decode(serialized)).slice();
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.buffer.get();

        this.transaction = new Transaction();

        int assetOffset = deserializeHeader();
        deserializeTypeSpecific(assetOffset);

        deserializeVersionOne();


        return this.transaction;
    }

    private int deserializeHeader() {
        transaction.version = this.buffer.get();
        transaction.network = this.buffer.get();
        if (transaction.version == 1) {
            transaction.type = TransactionType.values()[this.buffer.get()];
            transaction.timestamp = this.buffer.getInt();
        }else {
            transaction.typeGroup = TransactionTypeGroup.values()[this.buffer.getInt()];
            transaction.type = TransactionType.values()[this.buffer.getShort()];
            transaction.nonce = this.buffer.getLong();
        }
        byte[] senderPublicKey = new byte[33];
        this.buffer.get(senderPublicKey);
        transaction.senderPublicKey = Hex.encode(senderPublicKey);

        transaction.fee = this.buffer.getLong();

        int vendorFieldLength = this.buffer.get();
        if (vendorFieldLength > 0) {
            byte[] vendorFieldHex = new byte[vendorFieldLength];
            this.buffer.get(vendorFieldHex);
            transaction.vendorFieldHex = Hex.encode(vendorFieldHex);
        }

        if (transaction.version == 1){
            return (41 + 8 + 1) * 2 + vendorFieldLength * 2;
        }else {
            return 59 * 2 + vendorFieldLength * 2;
        }

    }

    private void deserializeTypeSpecific(int assetOffset) {
        switch (transaction.type) {
            case TRANSFER:
                new Transfer(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case SECOND_SIGNATURE_REGISTRATION:
                new SecondSignatureRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case DELEGATE_REGISTRATION:
                new DelegateRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case VOTE:
                new Vote(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case MULTI_SIGNATURE_REGISTRATION:
                new MultiSignatureRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case IPFS:
                new Ipfs(this.serialized,this.buffer,this.transaction).deserialize(assetOffset);
                break;
            case MULTI_PAYMENT:
                new MultiPayment(this.serialized,this.buffer,this.transaction).deserialize(assetOffset);
                break;
            case DELEGATE_RESIGNATION:
                new DelegateResignation(this.serialized,this.buffer,this.transaction).deserialize(assetOffset);
                break;
            case HTLC_LOCK:
                new HtlcLock(this.serialized,this.buffer,this.transaction).deserialize(assetOffset);
                break;
            case HTLC_CLAIM:
                new HtlcClaim(this.serialized,this.buffer,this.transaction).deserialize(assetOffset);
                break;
            case HTLC_REFUND:
                new HtlcRefund(this.serialized,this.buffer,this.transaction).deserialize(assetOffset);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void deserializeVersionOne() {
        if (transaction.secondSignature != null) {
            transaction.signSignature = transaction.secondSignature;
        }

        if (transaction.type == TransactionType.VOTE) {
            transaction.recipientId = Address.fromPublicKey(transaction.senderPublicKey, transaction.network);
        }

        if (transaction.type == TransactionType.MULTI_SIGNATURE_REGISTRATION) {
            for (int i = 0; i < transaction.asset.multisignature.keysgroup.size(); i++) {
                transaction.asset.multisignature.keysgroup.set(i, "+" + transaction.asset.multisignature.keysgroup.get(i));
            }
        }

        if (transaction.vendorFieldHex != null) {
            transaction.vendorField = new String(Hex.decode(transaction.vendorFieldHex));
        }

        if (transaction.id == null) {
            transaction.id = transaction.computeId();
        }

        if (transaction.type == TransactionType.SECOND_SIGNATURE_REGISTRATION || transaction.type == TransactionType.MULTI_SIGNATURE_REGISTRATION) {
            transaction.recipientId = Address.fromPublicKey(transaction.senderPublicKey, transaction.network);
        }

    }

}
