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
    public HashMap<String, Object> assetHashMap() {
        HashMap<String, Object> asset = new HashMap<>();
        asset.put("votes", this.asset.votes);
        return asset;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(1 + this.asset.votes.size() * 34);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        List<String> votes = new ArrayList<>(this.asset.votes);
        for (int i = 0; i < votes.size(); i++) {
            votes.set(
                    i, (votes.get(i).startsWith("+") ? "01" : "00") + (votes.get(i).substring(1)));
        }

        buffer.put((byte) votes.size());
        buffer.put(Hex.decode(String.join("", votes)));
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        int voteLength = buffer.get();
        for (int i = 0; i < voteLength; i++) {
            byte[] voteBuffer = new byte[34];
            buffer.get(voteBuffer);
            String vote = Hex.encode(voteBuffer);
            vote = (vote.startsWith("01") ? '+' : '-') + vote.substring(2);
            this.asset.votes.add(vote);
        }
    }
}
