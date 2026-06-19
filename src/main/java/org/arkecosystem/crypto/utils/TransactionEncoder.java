package org.arkecosystem.crypto.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.arkecosystem.crypto.utils.ProofOfPossession;

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

    public static String validatorRegistration(String passphrase) {
        ProofOfPossession.Result pop = ProofOfPossession.fromMnemonic(passphrase);
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.VALIDATOR_REGISTRATION,
                Arrays.asList("0x" + Hex.encode(pop.pk), "0x" + Hex.encode(pop.pop)));
    }

    public static String validatorResignation() {
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.VALIDATOR_RESIGNATION,
                Collections.emptyList());
    }

    public static String validatorUpdate(String passphrase) {
        ProofOfPossession.Result pop = ProofOfPossession.fromMnemonic(passphrase);
        return encode(
                ContractAbiType.CONSENSUS,
                AbiFunction.UPDATE_VALIDATOR,
                Arrays.asList("0x" + Hex.encode(pop.pk), "0x" + Hex.encode(pop.pop)));
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


}
