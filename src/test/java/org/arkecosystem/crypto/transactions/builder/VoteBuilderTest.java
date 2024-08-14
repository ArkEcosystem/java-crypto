package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class VoteBuilderTest {
    @Test
    void buildVote() {
        Transaction actual =
                new VoteBuilder()
                        .addVote(
                                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        List actualVotes = (List) actualAsset.get("votes");

        assertEquals(
                actualVotes,
                Arrays.asList(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"));
    }

    @Test
    void buildVotes() {
        Transaction actual =
                new VoteBuilder()
                        .addVotes(
                                Arrays.asList(
                                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"))
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        List actualVotes = (List) actualAsset.get("votes");

        assertEquals(
                actualVotes,
                Arrays.asList(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"));
    }

    @Test
    void buildUnvote() {
        Transaction actual =
                new VoteBuilder()
                        .addUnvote(
                                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed193")
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        List actualUnvotes = (List) actualAsset.get("unvotes");

        assertEquals(
                actualUnvotes,
                Arrays.asList(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed193"));
    }

    @Test
    void buildUnvoteVote() {
        Transaction actual =
                new VoteBuilder()
                        .addVotes(
                                Arrays.asList(
                                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"))
                        .addUnvotes(
                                Arrays.asList(
                                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed193"))
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        List actualVotes = (List) actualAsset.get("votes");
        List actualUnvotes = (List) actualAsset.get("unvotes");

        assertEquals(
                actualVotes,
                Arrays.asList(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"));

        assertEquals(
                actualUnvotes,
                Arrays.asList(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed193"));
    }

    @Test
    void buildVoteSecondSignature() {
        Transaction actual =
                new VoteBuilder()
                        .addVote(
                                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actual.verify());
        assertTrue(
                actual.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));
    }

    @Test
    void buildMultiSignature() {
        Transaction actual =
                new VoteBuilder()
                        .addVote(
                                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192")
                        .version(2)
                        .nonce(3)
                        .multiSign("secret 1", 0)
                        .multiSign("secret 2", 1)
                        .multiSign("secret 3", 2)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("signatures"));

        List<String> actualSignatures = (List<String>) actualHashMap.get("signatures");

        assertEquals(3, actualSignatures.size());
    }
}
