package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.UsernameRegistration;
import org.junit.jupiter.api.Test;

public class UsernameRegistrationBuilderTest extends AbstractTest {

    private static final String REGISTER_SELECTOR = "0x36a94134";

    @Test
    public void it_should_default_to_the_usernames_well_known_contract() {
        UsernameRegistrationBuilder builder = new UsernameRegistrationBuilder();

        assertEquals(ContractAddresses.USERNAMES.address(), builder.transaction.recipientAddress);
    }

    @Test
    public void it_should_encode_register_username_selector_and_argument() {
        UsernameRegistrationBuilder builder =
                new UsernameRegistrationBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(200_000)
                        .nonce(1L)
                        .username("alice");

        assertEquals("alice", builder.transaction.username);
        assertTrue(
                builder.transaction.data.startsWith(REGISTER_SELECTOR),
                "expected payload to start with register selector but was "
                        + builder.transaction.data);
    }

    @Test
    public void it_should_sign_and_verify_a_username_registration_transaction() {
        UsernameRegistrationBuilder builder =
                new UsernameRegistrationBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(200_000)
                        .nonce(1L)
                        .username("alice")
                        .sign(this.passphrase);

        assertNotNull(builder.transaction.signature);
        assertNotNull(builder.transaction.id);
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_round_trip_through_serialization() {
        UsernameRegistrationBuilder builder =
                new UsernameRegistrationBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(200_000)
                        .nonce(7L)
                        .username("bob_42")
                        .sign(this.passphrase);

        String serialized = Hex.encode(builder.transaction.serialize());
        AbstractTransaction restored = Deserializer.newDeserializer(serialized).deserialize();

        assertInstanceOf(UsernameRegistration.class, restored);
        UsernameRegistration registration = (UsernameRegistration) restored;
        assertEquals(builder.transaction.id, registration.id);
        assertEquals("bob_42", registration.username);
        assertEquals(
                builder.transaction.recipientAddress.toLowerCase(),
                registration.recipientAddress.toLowerCase());
    }

    @Test
    public void it_should_produce_empty_payload_when_no_username_is_set() {
        UsernameRegistrationBuilder builder = new UsernameRegistrationBuilder();

        assertEquals("", builder.transaction.data);
    }

    @Test
    public void it_should_reject_empty_username() {
        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new UsernameRegistrationBuilder().username(""));
        assertTrue(thrown.getMessage().contains("between 1 and 20"));
    }

    @Test
    public void it_should_reject_username_longer_than_20_characters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UsernameRegistrationBuilder().username("a_very_long_username_xx"));
    }

    @Test
    public void it_should_reject_uppercase_letters() {
        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new UsernameRegistrationBuilder().username("Alice"));
        assertTrue(thrown.getMessage().contains("lowercase"));
    }

    @Test
    public void it_should_reject_special_characters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UsernameRegistrationBuilder().username("alice!"));
    }

    @Test
    public void it_should_reject_leading_or_trailing_underscore() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UsernameRegistrationBuilder().username("_alice"));
        assertThrows(
                IllegalArgumentException.class,
                () -> new UsernameRegistrationBuilder().username("alice_"));
    }

    @Test
    public void it_should_reject_consecutive_underscores() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UsernameRegistrationBuilder().username("al__ice"));
    }
}
