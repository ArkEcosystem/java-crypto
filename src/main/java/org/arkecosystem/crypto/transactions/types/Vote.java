package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class Vote extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.VOTE.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        HashMap<String, Object> asset = new HashMap<>();
        asset.put("votes", this.asset.votes);
        asset.put("unvotes", this.asset.unvotes);
        return asset;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(
            (1 + this.asset.votes.size() * 33)
            + (1 + this.asset.unvotes.size() * 33)
        );

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        List<String> votes = new ArrayList<>(this.asset.votes);
        List<String> unvotes = new ArrayList<>(this.asset.unvotes);
        
        buffer.put((byte) votes.size());
        buffer.put(Hex.decode(String.join("", votes)));

        buffer.put((byte) unvotes.size());
        buffer.put(Hex.decode(String.join("", unvotes)));

        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        int voteLength = buffer.get();

        for (int i = 0; i < voteLength; i++) {
            byte[] voteBuffer = new byte[33];
            buffer.get(voteBuffer);
            String vote = Hex.encode(voteBuffer);
            this.asset.votes.add(vote);
        }

        int unvoteLength = buffer.get();

        for (int i = 0; i < unvoteLength; i++) {
            byte[] unvoteBuffer = new byte[33];
            buffer.get(unvoteBuffer);
            String unvote = Hex.encode(unvoteBuffer);
            this.asset.unvotes.add(unvote);
        }
    }
}
