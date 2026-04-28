package org.arkecosystem.crypto.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public abstract class AbiBase {

    protected List<Map<String, Object>> abi;

    public AbiBase() throws IOException {
        this(ContractAbiType.CONSENSUS, null);
    }

    public AbiBase(ContractAbiType type) throws IOException {
        this(type, null);
    }

    public AbiBase(ContractAbiType type, String path) throws IOException {
        String abiFilePath = contractAbiPath(type, path);
        Map<String, Object> decodedAbi = loadAbiJson(abiFilePath);
        this.abi = (List<Map<String, Object>>) decodedAbi.get("abi");
    }

    public static Map<String, String> methodIdentifiers(ContractAbiType type) throws IOException {
        return methodIdentifiers(type, null);
    }

    public static Map<String, String> methodIdentifiers(ContractAbiType type, String path)
            throws IOException {
        String abiFilePath = contractAbiPath(type, path);
        Map<String, Object> decodedAbi = loadAbiJson(abiFilePath);

        Object methodIdentifiers = decodedAbi.get("methodIdentifiers");
        if (!(methodIdentifiers instanceof Map)) {
            throw new RuntimeException(
                    "ABI JSON does not contain methodIdentifiers: " + abiFilePath);
        }

        return (Map<String, String>) methodIdentifiers;
    }

    protected static String[] getArrayComponents(String type) {
        Pattern pattern = Pattern.compile("^(.*)\\[(\\d*)\\]$");
        Matcher matcher = pattern.matcher(type);
        if (matcher.find()) {
            String innerType = matcher.group(1);
            String lengthStr = matcher.group(2);
            return new String[] {lengthStr, innerType};
        }
        return null;
    }

    protected String stripHexPrefix(String hex) {
        return Numeric.cleanHexPrefix(hex);
    }

    protected boolean isValidAddress(String address) {
        return address != null
                && address.startsWith("0x")
                && address.length() == 42
                && address.substring(2).matches("[0-9a-fA-F]+");
    }

    protected String keccak256(String input) {
        return Hash.sha3String(input);
    }

    protected String getFunctionSignature(Map<String, Object> abiItem) {
        String name = (String) abiItem.get("name");
        List<Map<String, Object>> inputs = (List<Map<String, Object>>) abiItem.get("inputs");
        List<String> types = new ArrayList<>();
        for (Map<String, Object> input : inputs) {
            types.add((String) input.get("type"));
        }
        return name + "(" + String.join(",", types) + ")";
    }

    protected String toFunctionSelector(Map<String, Object> abiItem) {
        String signature = getFunctionSignature(abiItem);
        String hash = keccak256(signature);
        return "0x" + stripHexPrefix(hash).substring(0, 8);
    }

    protected String concatHex(List<String> hexes) {
        StringBuilder result = new StringBuilder("0x");
        for (String hex : hexes) {
            if (hex == null || hex.isEmpty() || hex.equals("0x")) {
                continue;
            }
            result.append(stripHexPrefix(hex));
        }
        return result.toString();
    }

    protected static String contractAbiPath(ContractAbiType type, String path) {
        switch (type) {
            case CONSENSUS:
                return "Abi.Consensus.json";
            case MULTIPAYMENT:
                return "Abi.Multipayment.json";
            case USERNAMES:
                return "Abi.Usernames.json";
            case ERC20BATCH_TRANSFER:
                return "Abi.ERC20BatchTransfer.json";
            case TOKEN:
                return "Abi.Token.json";
            case CUSTOM:
                if (path == null || path.isEmpty()) {
                    throw new IllegalArgumentException(
                            "A non-empty path must be provided when using ContractAbiType.CUSTOM.");
                }
                return path;
            default:
                throw new IllegalArgumentException("Unhandled ContractAbiType: " + type.name());
        }
    }

    private static Map<String, Object> loadAbiJson(String path) throws IOException {
        InputStream stream = AbiBase.class.getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            if (Files.exists(Paths.get(path))) {
                stream = Files.newInputStream(Paths.get(path));
            } else {
                throw new RuntimeException("Unable to load ABI JSON: " + path);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> decoded = mapper.readValue(stream, Map.class);

        Object abi = decoded.get("abi");
        if (!(abi instanceof List)) {
            throw new RuntimeException("ABI JSON does not contain a valid abi array: " + path);
        }

        return decoded;
    }
}
