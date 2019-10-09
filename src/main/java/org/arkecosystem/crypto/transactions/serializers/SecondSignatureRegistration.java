package org.arkecosystem.crypto.transactions.serializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

public class SecondSignatureRegistration extends AbstractSerializer {
    public SecondSignatureRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    @Override
    public void serialize() {
        this.buffer.put(Hex.decode(this.transaction.asset.signature.publicKey));
    }
}
