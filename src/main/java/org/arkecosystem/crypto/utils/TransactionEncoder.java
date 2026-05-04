package org.arkecosystem.crypto.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.enums.ContractAbiType;

public final class TransactionEncoder {

    private TransactionEncoder() {}

    public static String multiPayment(List<String> recipients, List<BigInteger> amounts) {
        return encode(
                ContractAbiType.MULTIPAYMENT,
                AbiFunction.MULTIPAYMENT,
                Arrays.asList(recipients, amounts));
    }

    public static String tokenTransfer(String recipientAddress, BigInteger amount) {
        return encode(
                ContractAbiType.TOKEN,
                AbiFunction.TRANSFER,
                Arrays.asList(recipientAddress, amount));
    }

    public static String usernameRegistration(String username) {
        return encode(
                ContractAbiType.USERNAMES,
                AbiFunction.USERNAME_REGISTRATION,
                Collections.singletonList(username));
    }

    public static String usernameResignation() {
        return encode(
                ContractAbiType.USERNAMES,
                AbiFunction.USERNAME_RESIGNATION,
                Collections.emptyList());
    }

    public static String validatorRegistration(String validatorPublicKey) {
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.VALIDATOR_REGISTRATION,
                Collections.singletonList(addHexPrefix(validatorPublicKey)));
    }

    public static String validatorResignation() {
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.VALIDATOR_RESIGNATION,
                Collections.emptyList());
    }

    public static String updateValidator(String validatorPublicKey) {
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.UPDATE_VALIDATOR,
                Collections.singletonList(addHexPrefix(validatorPublicKey)));
    }

    public static String vote(String voteAddress) {
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.VOTE,
                Collections.singletonList(voteAddress));
    }

    public static String unvote() {
        return encode(ContractAbiType.CONSENSUS, AbiFunction.UNVOTE, Collections.emptyList());
    }

    private static String encode(ContractAbiType type, AbiFunction function, List<Object> args) {
        try {
            return new AbiEncoder(type).encodeFunctionCall(function.toString(), args);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error encoding " + function + " against " + type + " ABI", e);
        }
    }

    private static String addHexPrefix(String value) {
        return value.startsWith("0x") ? value : "0x" + value;
    }
}
