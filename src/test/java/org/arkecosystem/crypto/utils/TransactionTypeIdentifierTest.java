package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionTypeIdentifierTest {

    private Map<String, String> consensusMethods;
    private Map<String, String> multipaymentMethods;
    private Map<String, String> usernamesMethods;

    @BeforeEach
    void setUp() throws Exception {
        consensusMethods = AbiBase.methodIdentifiers(ContractAbiType.CONSENSUS);
        multipaymentMethods = AbiBase.methodIdentifiers(ContractAbiType.MULTIPAYMENT);
        usernamesMethods = AbiBase.methodIdentifiers(ContractAbiType.USERNAMES);
    }

    @Test
    void identifies_transfer_by_empty_payload() {
        assertTrue(TransactionTypeIdentifier.isTransfer(""));
        assertFalse(TransactionTypeIdentifier.isTransfer("0x"));
        assertFalse(TransactionTypeIdentifier.isTransfer("12345678"));
    }

    @Test
    void identifies_vote_signature() {
        String signature = consensusMethods.get("vote(address)");

        assertTrue(TransactionTypeIdentifier.isVote(signature));
        assertTrue(TransactionTypeIdentifier.isVote("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isVote("1234567"));
    }

    @Test
    void identifies_unvote_signature() {
        String signature = consensusMethods.get("unvote()");

        assertTrue(TransactionTypeIdentifier.isUnvote(signature));
        assertTrue(TransactionTypeIdentifier.isUnvote("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isUnvote("1234567"));
    }

    @Test
    void identifies_multipayment_signature() {
        String signature = multipaymentMethods.get("pay(address[],uint256[])");

        assertTrue(TransactionTypeIdentifier.isMultiPayment(signature));
        assertTrue(TransactionTypeIdentifier.isMultiPayment("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isMultiPayment("1234567"));
    }

    @Test
    void identifies_username_registration_signature() {
        String signature = usernamesMethods.get("registerUsername(string)");

        assertTrue(TransactionTypeIdentifier.isUsernameRegistration(signature));
        assertTrue(TransactionTypeIdentifier.isUsernameRegistration("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isUsernameRegistration("1234567"));
    }

    @Test
    void identifies_username_resignation_signature() {
        String signature = usernamesMethods.get("resignUsername()");

        assertTrue(TransactionTypeIdentifier.isUsernameResignation(signature));
        assertTrue(TransactionTypeIdentifier.isUsernameResignation("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isUsernameResignation("1234567"));
    }

    @Test
    void identifies_validator_registration_signature() {
        String signature = consensusMethods.get("registerValidator(bytes)");

        assertTrue(TransactionTypeIdentifier.isValidatorRegistration(signature));
        assertTrue(TransactionTypeIdentifier.isValidatorRegistration("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isValidatorRegistration("1234567"));
    }

    @Test
    void identifies_validator_resignation_signature() {
        String signature = consensusMethods.get("resignValidator()");

        assertTrue(TransactionTypeIdentifier.isValidatorResignation(signature));
        assertTrue(TransactionTypeIdentifier.isValidatorResignation("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isValidatorResignation("1234567"));
    }

    @Test
    void identifies_update_validator_signature() {
        String signature = consensusMethods.get("updateValidator(bytes)");

        assertTrue(TransactionTypeIdentifier.isUpdateValidator(signature));
        assertTrue(TransactionTypeIdentifier.isUpdateValidator("0x" + signature));
        assertFalse(TransactionTypeIdentifier.isUpdateValidator("1234567"));
    }

    @Test
    void identifies_token_transfer_payloads() {
        assertTrue(
                TransactionTypeIdentifier.isTokenTransfer(
                        "0xa9059cbb000000000000000000000000a5cc0bfeb09742c5e4c610f2ebaab82eb142ca10000000000000000000000000000000000000009bd2ffdd71438a49e803314000"));
        assertFalse(TransactionTypeIdentifier.isTokenTransfer("0x" + "0".repeat(64)));
        assertFalse(TransactionTypeIdentifier.isTokenTransfer("1234567"));
    }
}
