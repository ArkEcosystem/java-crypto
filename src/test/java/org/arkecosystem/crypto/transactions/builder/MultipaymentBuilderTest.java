package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Multipayment;
import org.junit.jupiter.api.Test;

public class MultipaymentBuilderTest extends AbstractTest {

    private static final String PAY_SELECTOR = "0x084ce708";
    private static final String RECIPIENT_A = "0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A";
    private static final String RECIPIENT_B = "0x512F366D524157BcF734546eB29a6d687B762255";

    @Test
    public void it_should_default_to_the_multipayment_well_known_contract() {
        MultipaymentBuilder builder = new MultipaymentBuilder();

        assertEquals(
                ContractAddresses.MULTIPAYMENT.address(), builder.transaction.recipientAddress);
    }

    @Test
    public void it_should_aggregate_payment_value_and_encode_pay_selector() {
        MultipaymentBuilder builder =
                new MultipaymentBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(200_000)
                        .nonce(1L)
                        .pay(RECIPIENT_A, new BigInteger("100000000"))
                        .pay(RECIPIENT_B, new BigInteger("200000000"));

        assertEquals("300000000", builder.transaction.value);
        assertTrue(
                builder.transaction.data.startsWith(PAY_SELECTOR),
                "expected payload to start with pay selector but was " + builder.transaction.data);
        assertEquals(2, builder.transaction.multipaymentRecipients.size());
        assertEquals(2, builder.transaction.multipaymentAmounts.size());
    }

    @Test
    public void it_should_sign_and_verify_a_multipayment_transaction() {
        MultipaymentBuilder builder =
                new MultipaymentBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(200_000)
                        .nonce(1L)
                        .pay(RECIPIENT_A, new BigInteger("100000000"))
                        .pay(RECIPIENT_B, new BigInteger("200000000"))
                        .sign(this.passphrase);

        assertNotNull(builder.transaction.signature);
        assertNotNull(builder.transaction.id);
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_round_trip_through_serialization() throws Exception {
        MultipaymentBuilder builder =
                new MultipaymentBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(200_000)
                        .nonce(7L)
                        .pay(RECIPIENT_A, new BigInteger("100000000"))
                        .pay(RECIPIENT_B, new BigInteger("200000000"))
                        .sign(this.passphrase);

        String serialized = Hex.encode(builder.transaction.serialize());
        AbstractTransaction restored = Deserializer.newDeserializer(serialized).deserialize();

        assertInstanceOf(Multipayment.class, restored);
        Multipayment multipayment = (Multipayment) restored;
        assertEquals(builder.transaction.id, multipayment.id);
        assertEquals(builder.transaction.value, multipayment.value);
        assertEquals(
                builder.transaction.recipientAddress.toLowerCase(),
                multipayment.recipientAddress.toLowerCase());
        assertEquals(2, multipayment.multipaymentRecipients.size());
        assertEquals(
                RECIPIENT_A.toLowerCase(),
                multipayment.multipaymentRecipients.get(0).toLowerCase());
        assertEquals(
                RECIPIENT_B.toLowerCase(),
                multipayment.multipaymentRecipients.get(1).toLowerCase());
        assertEquals(new BigInteger("100000000"), multipayment.multipaymentAmounts.get(0));
        assertEquals(new BigInteger("200000000"), multipayment.multipaymentAmounts.get(1));
    }

    @Test
    public void it_should_produce_empty_payload_when_no_payments_added() {
        MultipaymentBuilder builder = new MultipaymentBuilder();

        assertEquals("", builder.transaction.data);
        assertEquals("0", builder.transaction.value);
    }
}
