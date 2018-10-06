package org.arkecosystem.crypto.transactions.deserializers


import java.nio.ByteBuffer

class Vote extends AbstractDeserializer {
    Vote(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction)
    }

    void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2 as int)
        this.transaction.asset = [votes: []]

        int voteLength = this.buffer.get() & 0xff

        for (int i = 0; i < voteLength; i++) {
            def vote = this.serialized.substring(assetOffset + 2 + i * 2 * 34, assetOffset + 2 + (i + 1) * 2 * 34)
            vote = "${vote[1] == '1' ? '+' : '-'}${vote.substring(2)}"

            transaction.asset.votes.add(vote)
        }

        this.transaction.parseSignatures(this.serialized, assetOffset + 2 + voteLength * 34 * 2)
    }
}
