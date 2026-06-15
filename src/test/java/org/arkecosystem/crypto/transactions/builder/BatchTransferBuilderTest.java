package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.EvmCall;
import org.junit.jupiter.api.Test;

public class BatchTransferBuilderTest extends AbstractTest {

    private static final String BATCH_TRANSFER_SELECTOR = "4885b254";
    private static final String RECIPIENT_A = "0x6F0182a0cc707b055322CcF6d4CB6a5Aff1aEb22";
    private static final String RECIPIENT_B = "0xc3bbe9b1cee1ff85ad72b87414b0e9b7f2366763";
    private static final String TOKEN_ADDRESS = "0xdAC17F958D2ee523a2206206994597C13D831ec7";

    @Test
    public void it_should_default_to_the_batch_transfer_well_known_contract() {
        BatchTransferBuilder builder = new BatchTransferBuilder();

        assertEquals(
                ContractAddresses.BATCH_TRANSFER.address(), builder.transaction.recipientAddress);
    }

    @Test
    public void it_should_encode_the_batch_transfer_payload() throws Exception {
        Map<String, Object> fixture = loadFixture("batch-transfer");
        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        BatchTransferBuilder builder =
                new BatchTransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21_000)
                        .nonce(1L)
                        .tokenAddress(TOKEN_ADDRESS)
                        .addRecipient(RECIPIENT_A, new BigInteger("100000"))
                        .addRecipient(RECIPIENT_B, new BigInteger("200000"))
                        .sign(this.passphrase);

        assertEquals(data.get("data"), builder.transaction.data);
        assertTrue(builder.transaction.data.startsWith(BATCH_TRANSFER_SELECTOR));
        assertEquals("0", builder.transaction.value);
    }

    @Test
    public void it_should_sign_and_verify_a_batch_transfer_transaction() {
        BatchTransferBuilder builder =
                new BatchTransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21_000)
                        .nonce(1L)
                        .tokenAddress(TOKEN_ADDRESS)
                        .addRecipient(RECIPIENT_A, new BigInteger("100000"))
                        .addRecipient(RECIPIENT_B, new BigInteger("200000"))
                        .sign(this.passphrase);

        assertNotNull(builder.transaction.signature);
        assertNotNull(builder.transaction.id);
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_encode_a_single_recipient() {
        BatchTransferBuilder builder =
                new BatchTransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21_000)
                        .nonce(1L)
                        .tokenAddress(TOKEN_ADDRESS)
                        .addRecipient(RECIPIENT_A, new BigInteger("100000"))
                        .sign(this.passphrase);

        assertTrue(builder.transaction.data.startsWith(BATCH_TRANSFER_SELECTOR));
        assertEquals("0", builder.transaction.value);
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_encode_large_amounts() {
        BatchTransferBuilder builder =
                new BatchTransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21_000)
                        .nonce(1L)
                        .tokenAddress(TOKEN_ADDRESS)
                        .addRecipient(RECIPIENT_A, new BigInteger("1000000000000000000000"))
                        .sign(this.passphrase);

        assertTrue(builder.verify());
    }

    @Test
    public void it_should_round_trip_through_serialization() throws Exception {
        BatchTransferBuilder builder =
                new BatchTransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21_000)
                        .nonce(7L)
                        .tokenAddress(TOKEN_ADDRESS)
                        .addRecipient(RECIPIENT_A, new BigInteger("100000"))
                        .addRecipient(RECIPIENT_B, new BigInteger("200000"))
                        .sign(this.passphrase);

        String serialized = Hex.encode(builder.transaction.serialize());
        AbstractTransaction restored = Deserializer.newDeserializer(serialized).deserialize();

        assertInstanceOf(EvmCall.class, restored);
        assertEquals(builder.transaction.id, restored.id);
        assertEquals(builder.transaction.data, restored.data);
        assertEquals(
                builder.transaction.recipientAddress.toLowerCase(),
                restored.recipientAddress.toLowerCase());
    }

    @Test
    public void it_should_throw_when_signing_with_no_recipients() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                new BatchTransferBuilder()
                                        .gasPrice(5_000_000_000L)
                                        .gasLimit(21_000)
                                        .nonce(1L)
                                        .tokenAddress(TOKEN_ADDRESS)
                                        .sign(this.passphrase));

        assertEquals("Must add at least one recipient before encoding.", exception.getMessage());
    }

    @Test
    public void it_should_throw_when_signing_without_a_token_address() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                new BatchTransferBuilder()
                                        .gasPrice(5_000_000_000L)
                                        .gasLimit(21_000)
                                        .nonce(1L)
                                        .addRecipient(RECIPIENT_A, new BigInteger("100000"))
                                        .sign(this.passphrase));

        assertEquals("Must set tokenAddress before encoding.", exception.getMessage());
    }
}
