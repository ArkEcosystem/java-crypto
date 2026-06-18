package org.arkecosystem.crypto.utils;

import java.util.HashMap;
import java.util.Map;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.web3j.utils.Numeric;

public class TransactionTypeIdentifier {

    private static final String TRANSFER_SIGNATURE = "";

    private static Map<String, String> signatures;

    public static boolean isTransfer(String data) {
        return TRANSFER_SIGNATURE.equals(data);
    }

    public static boolean isVote(String data) {
        return startsWithSignature(data, signatures().get("vote"));
    }

    public static boolean isUnvote(String data) {
        return startsWithSignature(data, signatures().get("unvote"));
    }

    public static boolean isMultiPayment(String data) {
        return startsWithSignature(data, signatures().get("multiPayment"));
    }

    public static boolean isUsernameRegistration(String data) {
        return startsWithSignature(data, signatures().get("registerUsername"));
    }

    public static boolean isUsernameResignation(String data) {
        return startsWithSignature(data, signatures().get("resignUsername"));
    }

    public static boolean isValidatorRegistration(String data) {
        return startsWithSignature(data, signatures().get("registerValidator"));
    }

    public static boolean isValidatorResignation(String data) {
        return startsWithSignature(data, signatures().get("resignValidator"));
    }

    public static boolean isUpdateValidator(String data) {
        return startsWithSignature(data, signatures().get("updateValidator"));
    }

    public static boolean isTokenTransfer(String data) {
        Map<String, Object> decodedData = decodeTokenFunction(data);
        return decodedData != null && "transfer".equals(decodedData.get("functionName"));
    }

    private static boolean startsWithSignature(String data, String signature) {
        if (data == null || signature == null) {
            return false;
        }
        return Numeric.cleanHexPrefix(data).toLowerCase().startsWith(signature.toLowerCase());
    }

    private static synchronized Map<String, String> signatures() {
        if (signatures != null) {
            return signatures;
        }

        try {
            Map<String, String> consensusMethods =
                    AbiBase.methodIdentifiers(ContractAbiType.CONSENSUS);
            Map<String, String> multipaymentMethods =
                    AbiBase.methodIdentifiers(ContractAbiType.MULTIPAYMENT);
            Map<String, String> usernamesMethods =
                    AbiBase.methodIdentifiers(ContractAbiType.USERNAMES);

            Map<String, String> map = new HashMap<>();
            map.put("multiPayment", multipaymentMethods.get("pay(address[],uint256[])"));
            map.put("registerUsername", usernamesMethods.get("registerUsername(string)"));
            map.put("resignUsername", usernamesMethods.get("resignUsername()"));
            map.put("registerValidator", consensusMethods.get("registerValidator(bytes,bytes)"));
            map.put("resignValidator", consensusMethods.get("resignValidator()"));
            map.put("vote", consensusMethods.get("vote(address)"));
            map.put("unvote", consensusMethods.get("unvote()"));
            map.put("updateValidator", consensusMethods.get("updateValidator(bytes,bytes)"));
            map.put("transfer", "transfer");

            signatures = map;
            return signatures;
        } catch (Exception e) {
            throw new RuntimeException("Unable to load method identifiers", e);
        }
    }

    private static Map<String, Object> decodeTokenFunction(String data) {
        try {
            return new AbiDecoder(ContractAbiType.TOKEN).decodeFunctionData(data);
        } catch (Exception e) {
            return null;
        }
    }
}
