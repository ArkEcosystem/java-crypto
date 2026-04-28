package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

class AbiJsonFilesTest {

    private static final List<String> ABI_FILES =
            List.of(
                    "Abi.Consensus.json",
                    "Abi.Multipayment.json",
                    "Abi.Usernames.json",
                    "Abi.ERC20BatchTransfer.json",
                    "Abi.Token.json");

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadJson(String name) throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
        assertNotNull(stream, "ABI resource missing: " + name);
        return new ObjectMapper().readValue(stream, Map.class);
    }

    @Test
    void every_abi_file_parses_and_has_an_abi_array() throws Exception {
        for (String file : ABI_FILES) {
            Map<String, Object> json = loadJson(file);
            Object abi = json.get("abi");
            assertTrue(abi instanceof List, file + " must contain an abi array");
            assertFalse(((List<?>) abi).isEmpty(), file + " abi array must not be empty");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void method_identifiers_match_keccak256_of_signature() throws Exception {
        for (String file : ABI_FILES) {
            Map<String, Object> json = loadJson(file);
            Object identifiers = json.get("methodIdentifiers");
            if (!(identifiers instanceof Map)) {
                continue;
            }

            for (Map.Entry<String, String> entry : ((Map<String, String>) identifiers).entrySet()) {
                String signature = entry.getKey();
                String expectedSelector = entry.getValue();
                String actualSelector =
                        Numeric.cleanHexPrefix(Hash.sha3String(signature)).substring(0, 8);
                assertEquals(
                        actualSelector,
                        expectedSelector,
                        file + " selector mismatch for " + signature);
            }
        }
    }

    @Test
    void abi_encoder_loads_every_contract_abi_type() throws Exception {
        for (org.arkecosystem.crypto.enums.ContractAbiType type :
                org.arkecosystem.crypto.enums.ContractAbiType.values()) {
            if (type == org.arkecosystem.crypto.enums.ContractAbiType.CUSTOM) {
                continue;
            }
            new AbiEncoder(type);
        }
    }

    @Test
    void abi_decoder_decodes_consensus_payload() throws Exception {
        Map<String, Object> result =
                new AbiDecoder()
                        .decodeFunctionData(
                                "0x6dd7d8ea000000000000000000000000512f366d524157bcf734546eb29a6d687b762255");

        assertEquals("vote", result.get("functionName"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void consensus_exposes_expected_method_signatures() throws Exception {
        Map<String, Object> json = loadJson("Abi.Consensus.json");
        Map<String, String> identifiers = (Map<String, String>) json.get("methodIdentifiers");

        assertEquals("6dd7d8ea", identifiers.get("vote(address)"));
        assertNotNull(identifiers.get("unvote()"));
        assertNotNull(identifiers.get("registerValidator(bytes)"));
        assertNotNull(identifiers.get("resignValidator()"));
        assertNotNull(identifiers.get("updateValidator(bytes)"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void multipayment_exposes_pay_signature() throws Exception {
        Map<String, Object> json = loadJson("Abi.Multipayment.json");
        Map<String, String> identifiers = (Map<String, String>) json.get("methodIdentifiers");

        assertNotNull(identifiers.get("pay(address[],uint256[])"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void usernames_exposes_register_and_resign_signatures() throws Exception {
        Map<String, Object> json = loadJson("Abi.Usernames.json");
        Map<String, String> identifiers = (Map<String, String>) json.get("methodIdentifiers");

        assertNotNull(identifiers.get("registerUsername(string)"));
        assertNotNull(identifiers.get("resignUsername()"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void token_exposes_erc20_signatures() throws Exception {
        Map<String, Object> json = loadJson("Abi.Token.json");
        Map<String, String> identifiers = (Map<String, String>) json.get("methodIdentifiers");

        // canonical ERC20 selectors
        assertEquals("a9059cbb", identifiers.get("transfer(address,uint256)"));
        assertEquals("095ea7b3", identifiers.get("approve(address,uint256)"));
    }
}
