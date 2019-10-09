package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vote extends AbstractSerializer {
    public Vote(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        List<String> votes = new ArrayList<>(transaction.asset.votes);

        for (int i = 0; i < votes.size(); i++) {
            votes.set(i, (votes.get(i).startsWith("+") ? "01" : "00") + (votes.get(i).substring(1)));
        }
        this.buffer.put((byte) votes.size());

        this.buffer.put(Hex.decode(String.join("", votes)));

    }

}
