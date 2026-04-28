package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.junit.jupiter.api.Test;

class TransactionTypeIdentifierTest {

    private static final String VOTE = "6dd7d8ea";
    private static final String UNVOTE = "3174b689";
    private static final String REGISTER_VALIDATOR = "602a9eee";
    private static final String RESIGN_VALIDATOR = "b85f5da2";
    private static final String UPDATE_VALIDATOR = "5a8eed73";
    private static final String PAY = "084ce708";
    private static final String REGISTER_USERNAME = "36a94134";
    private static final String RESIGN_USERNAME = "ebed6dab";
    private static final String ERC20_TRANSFER = "a9059cbb";

    @Test
    void identifies_transfer_only_for_empty_payload() {
        assertTrue(TransactionTypeIdentifier.isTransfer(""));
        assertFalse(TransactionTypeIdentifier.isTransfer("0x"));
        assertFalse(TransactionTypeIdentifier.isTransfer(VOTE));
    }

    @Test
    void identifies_vote_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isVote(VOTE));
        assertTrue(TransactionTypeIdentifier.isVote("0x" + VOTE));
        assertTrue(
                TransactionTypeIdentifier.isVote(
                        "0x6dd7d8ea000000000000000000000000512f366d524157bcf734546eb29a6d687b762255"));
        assertFalse(TransactionTypeIdentifier.isVote(UNVOTE));
        assertFalse(TransactionTypeIdentifier.isVote(""));
        assertFalse(TransactionTypeIdentifier.isVote("1234567"));
    }

    @Test
    void identifies_unvote_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isUnvote(UNVOTE));
        assertTrue(TransactionTypeIdentifier.isUnvote("0x" + UNVOTE));
        assertFalse(TransactionTypeIdentifier.isUnvote(VOTE));
    }

    @Test
    void identifies_multipayment_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isMultiPayment(PAY));
        assertTrue(TransactionTypeIdentifier.isMultiPayment("0x" + PAY));
        assertFalse(TransactionTypeIdentifier.isMultiPayment(VOTE));
    }

    @Test
    void identifies_username_registration_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isUsernameRegistration(REGISTER_USERNAME));
        assertTrue(TransactionTypeIdentifier.isUsernameRegistration("0x" + REGISTER_USERNAME));
        assertFalse(TransactionTypeIdentifier.isUsernameRegistration(RESIGN_USERNAME));
    }

    @Test
    void identifies_username_resignation_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isUsernameResignation(RESIGN_USERNAME));
        assertTrue(TransactionTypeIdentifier.isUsernameResignation("0x" + RESIGN_USERNAME));
        assertFalse(TransactionTypeIdentifier.isUsernameResignation(REGISTER_USERNAME));
    }

    @Test
    void identifies_validator_registration_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isValidatorRegistration(REGISTER_VALIDATOR));
        assertTrue(TransactionTypeIdentifier.isValidatorRegistration("0x" + REGISTER_VALIDATOR));
        assertFalse(TransactionTypeIdentifier.isValidatorRegistration(RESIGN_VALIDATOR));
    }

    @Test
    void identifies_validator_resignation_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isValidatorResignation(RESIGN_VALIDATOR));
        assertTrue(TransactionTypeIdentifier.isValidatorResignation("0x" + RESIGN_VALIDATOR));
        assertFalse(TransactionTypeIdentifier.isValidatorResignation(REGISTER_VALIDATOR));
    }

    @Test
    void identifies_update_validator_via_canonical_selector() {
        assertTrue(TransactionTypeIdentifier.isUpdateValidator(UPDATE_VALIDATOR));
        assertTrue(TransactionTypeIdentifier.isUpdateValidator("0x" + UPDATE_VALIDATOR));
        assertFalse(TransactionTypeIdentifier.isUpdateValidator(REGISTER_VALIDATOR));
    }

    @Test
    @SuppressWarnings("unchecked")
    void identifies_token_transfer_and_decodes_canonical_arguments() throws Exception {
        String address = "0xA5cc0BfeB09742C5e4C610F2EBaab82Eb142Ca10";
        BigInteger amount = new BigInteger("47000000000000000000000000000000");

        String payload =
                new AbiEncoder(ContractAbiType.TOKEN)
                        .encodeFunctionCall("transfer", Arrays.asList(address, amount));

        assertTrue(payload.toLowerCase().startsWith("0x" + ERC20_TRANSFER));
        assertTrue(TransactionTypeIdentifier.isTokenTransfer(payload));

        Map<String, Object> decoded =
                new AbiDecoder(ContractAbiType.TOKEN).decodeFunctionData(payload);
        List<Object> args = (List<Object>) decoded.get("args");
        assertEquals("transfer", decoded.get("functionName"));
        assertEquals(address.toLowerCase(), ((String) args.get(0)).toLowerCase());
        assertEquals(amount.toString(), args.get(1));
    }

    @Test
    void rejects_token_transfer_for_non_transfer_payloads() {
        assertFalse(TransactionTypeIdentifier.isTokenTransfer("0x" + "0".repeat(64)));
        assertFalse(TransactionTypeIdentifier.isTokenTransfer("1234567"));
        assertFalse(
                TransactionTypeIdentifier.isTokenTransfer(
                        "0x6dd7d8ea000000000000000000000000512f366d524157bcf734546eb29a6d687b762255"));
    }

    @Test
    void selectors_are_mutually_exclusive() {
        assertFalse(TransactionTypeIdentifier.isVote(UNVOTE));
        assertFalse(TransactionTypeIdentifier.isUnvote(VOTE));
        assertFalse(TransactionTypeIdentifier.isMultiPayment(VOTE));
        assertFalse(TransactionTypeIdentifier.isValidatorRegistration(VOTE));
        assertFalse(TransactionTypeIdentifier.isValidatorResignation(VOTE));
        assertFalse(TransactionTypeIdentifier.isUpdateValidator(VOTE));
        assertFalse(TransactionTypeIdentifier.isUsernameRegistration(VOTE));
        assertFalse(TransactionTypeIdentifier.isUsernameResignation(VOTE));
    }

    @Test
    void matching_is_case_insensitive() {
        assertTrue(TransactionTypeIdentifier.isVote(VOTE.toUpperCase()));
        assertTrue(TransactionTypeIdentifier.isVote("0x" + VOTE.toUpperCase()));
    }
}
