package org.arkecosystem.crypto.transactions.deserializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;

public class SecondSignatureRegistration extends AbstractDeserializer {
    public SecondSignatureRegistration(
            String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    @Override
    public void deserialize(int assetOffset) {
        this.transaction.asset.signature.publicKey =
                this.serialized.substring(assetOffset, assetOffset + 66);
        this.transaction.parseSignatures(this.serialized, assetOffset + 66);
    }
}
