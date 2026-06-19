package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.junit.jupiter.api.Test;

public class TransactionEncoderTest {

    private static final String VALIDATOR_PASSPHRASE =
            "gold favorite math anchor detect march purpose such sausage crucial reform novel"
                    + " connect misery update episode invite salute barely garbage exclude winner"
                    + " visa cruise";

    @Test
    public void it_should_encode_a_vote_payload() throws Exception {
        String address = "0xC3bBE9B1CeE1ff85Ad72b87414B0E9B7F2366763";

        String payload = TransactionEncoder.vote(address);

        Map<String, Object> decoded = new AbiDecoder().decodeFunctionData(payload);
        assertEquals("vote", decoded.get("functionName"));
        assertEquals(Arrays.asList(address), decoded.get("args"));
    }

    @Test
    public void it_should_encode_an_unvote_payload() throws Exception {
        String payload = TransactionEncoder.unvote();

        Map<String, Object> decoded = new AbiDecoder().decodeFunctionData(payload);
        assertEquals("unvote", decoded.get("functionName"));
    }

    @Test
    public void it_should_encode_a_validator_registration_payload() throws Exception {
        String payload = TransactionEncoder.validatorRegistration(VALIDATOR_PASSPHRASE);

        Map<String, Object> decoded = new AbiDecoder().decodeFunctionData(payload);
        assertEquals("registerValidator", decoded.get("functionName"));
    }

    @Test
    public void it_should_encode_a_validator_resignation_payload() throws Exception {
        String payload = TransactionEncoder.validatorResignation();

        Map<String, Object> decoded = new AbiDecoder().decodeFunctionData(payload);
        assertEquals("resignValidator", decoded.get("functionName"));
    }

    @Test
    public void it_should_encode_a_validator_update_payload() throws Exception {
        String payload = TransactionEncoder.validatorUpdate(VALIDATOR_PASSPHRASE);

        Map<String, Object> decoded = new AbiDecoder().decodeFunctionData(payload);
        assertEquals("updateValidator", decoded.get("functionName"));
    }

    @Test
    public void it_should_encode_a_username_registration_payload() throws Exception {
        String payload = TransactionEncoder.usernameRegistration("alice");

        Map<String, Object> decoded =
                new AbiDecoder(ContractAbiType.USERNAMES).decodeFunctionData(payload);
        assertEquals("registerUsername", decoded.get("functionName"));
        assertEquals(Arrays.asList("alice"), decoded.get("args"));
    }

    @Test
    public void it_should_encode_a_username_resignation_payload() throws Exception {
        String payload = TransactionEncoder.usernameResignation();

        Map<String, Object> decoded =
                new AbiDecoder(ContractAbiType.USERNAMES).decodeFunctionData(payload);
        assertEquals("resignUsername", decoded.get("functionName"));
    }

    @Test
    public void it_should_encode_a_multipayment_payload() throws Exception {
        String payload =
                TransactionEncoder.multiPayment(
                        Arrays.asList(
                                "0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A",
                                "0x512F366D524157BcF734546eB29a6d687B762255"),
                        Arrays.asList(new BigInteger("100000000"), new BigInteger("200000000")));

        assertTrue(payload.startsWith("0x084ce708"));
    }

    @Test
    public void it_should_encode_a_token_transfer_payload() throws Exception {
        String payload =
                TransactionEncoder.tokenTransfer(
                        "0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A",
                        new BigInteger("1000000000000000000"));

        Map<String, Object> decoded =
                new AbiDecoder(ContractAbiType.TOKEN).decodeFunctionData(payload);
        assertEquals("transfer", decoded.get("functionName"));
    }
}
