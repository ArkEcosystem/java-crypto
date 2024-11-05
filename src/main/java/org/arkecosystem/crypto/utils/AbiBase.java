package org.arkecosystem.crypto.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public abstract class AbiBase {

    protected List<Map<String, Object>> abi;

    public AbiBase() throws IOException {
        String abiFilePath = "/Abi.Consensus.json";

        ObjectMapper mapper = new ObjectMapper();
        InputStream abiInputStream = getClass().getResourceAsStream(abiFilePath);
        Map<String, Object> abiJson = mapper.readValue(abiInputStream, Map.class);
        this.abi = (List<Map<String, Object>>) abiJson.get("abi");
    }

    protected String[] getArrayComponents(String type) {
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
}
