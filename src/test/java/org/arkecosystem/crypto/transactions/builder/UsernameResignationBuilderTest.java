package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.UsernameResignation;
import org.junit.jupiter.api.Test;

public class UsernameResignationBuilderTest extends AbstractTest {

    private static final String RESIGN_SELECTOR = "0xebed6dab";

    @Test
    public void it_should_default_to_the_usernames_well_known_contract() {
        UsernameResignationBuilder builder = new UsernameResignationBuilder();

        assertEquals(ContractAddresses.USERNAMES.address(), builder.transaction.recipientAddress);
    }

    @Test
    public void it_should_encode_resign_username_selector() {
        UsernameResignationBuilder builder =
                new UsernameResignationBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(100_000)
                        .nonce(1L);

        builder.transaction.refreshPayloadData();

        assertEquals(RESIGN_SELECTOR, builder.transaction.data);
    }

    @Test
    public void it_should_sign_and_verify_a_username_resignation_transaction() {
        UsernameResignationBuilder builder =
                new UsernameResignationBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(100_000)
                        .nonce(1L)
                        .sign(this.passphrase);

        assertNotNull(builder.transaction.signature);
        assertNotNull(builder.transaction.id);
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_round_trip_through_serialization() {
        UsernameResignationBuilder builder =
                new UsernameResignationBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(100_000)
                        .nonce(7L)
                        .sign(this.passphrase);

        String serialized = Hex.encode(builder.transaction.serialize());
        AbstractTransaction restored = Deserializer.newDeserializer(serialized).deserialize();

        assertInstanceOf(UsernameResignation.class, restored);
        assertEquals(builder.transaction.id, restored.id);
        assertEquals(
                builder.transaction.recipientAddress.toLowerCase(),
                restored.recipientAddress.toLowerCase());
    }
}
