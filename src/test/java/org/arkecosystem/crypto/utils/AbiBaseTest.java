package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.junit.jupiter.api.Test;

class AbiBaseTest {

    @Test
    void method_identifiers_loads_consensus_signatures() throws Exception {
        Map<String, String> identifiers = AbiBase.methodIdentifiers(ContractAbiType.CONSENSUS);

        assertEquals("6dd7d8ea", identifiers.get("vote(address)"));
        assertEquals("3174b689", identifiers.get("unvote()"));
        assertEquals("602a9eee", identifiers.get("registerValidator(bytes)"));
    }

    @Test
    void method_identifiers_loads_multipayment_signatures() throws Exception {
        Map<String, String> identifiers = AbiBase.methodIdentifiers(ContractAbiType.MULTIPAYMENT);

        assertEquals("084ce708", identifiers.get("pay(address[],uint256[])"));
    }

    @Test
    void method_identifiers_loads_usernames_signatures() throws Exception {
        Map<String, String> identifiers = AbiBase.methodIdentifiers(ContractAbiType.USERNAMES);

        assertEquals("36a94134", identifiers.get("registerUsername(string)"));
        assertEquals("ebed6dab", identifiers.get("resignUsername()"));
    }

    @Test
    void custom_type_requires_a_non_empty_path() {
        assertThrows(IllegalArgumentException.class, () -> new AbiEncoder(ContractAbiType.CUSTOM));
        assertThrows(
                IllegalArgumentException.class, () -> new AbiEncoder(ContractAbiType.CUSTOM, ""));
        assertThrows(
                IllegalArgumentException.class,
                () -> AbiBase.methodIdentifiers(ContractAbiType.CUSTOM, null));
    }

    @Test
    void custom_type_loads_external_abi_path(@org.junit.jupiter.api.io.TempDir Path tempDir)
            throws Exception {
        Path file = tempDir.resolve("custom.json");
        Files.writeString(
                file,
                "{\n"
                        + "  \"abi\": [{\"type\":\"function\",\"name\":\"vote\",\"inputs\":[{\"type\":\"address\",\"name\":\"validator\"}]}],\n"
                        + "  \"methodIdentifiers\": {\"vote(address)\":\"6dd7d8ea\"}\n"
                        + "}");

        AbiEncoder encoder = new AbiEncoder(ContractAbiType.CUSTOM, file.toString());
        assertNotNull(encoder);

        Map<String, String> identifiers =
                AbiBase.methodIdentifiers(ContractAbiType.CUSTOM, file.toString());
        assertEquals("6dd7d8ea", identifiers.get("vote(address)"));
    }

    @Test
    void load_throws_when_file_is_missing() {
        assertThrows(
                RuntimeException.class,
                () -> new AbiEncoder(ContractAbiType.CUSTOM, "does-not-exist.json"));
    }

    @Test
    void load_throws_when_abi_array_is_missing(@org.junit.jupiter.api.io.TempDir Path tempDir)
            throws Exception {
        Path file = tempDir.resolve("invalid.json");
        Files.writeString(file, "{\"contractName\":\"X\"}");

        RuntimeException error =
                assertThrows(
                        RuntimeException.class,
                        () -> new AbiEncoder(ContractAbiType.CUSTOM, file.toString()));
        assertEquals("ABI JSON does not contain a valid abi array: " + file, error.getMessage());
    }

    @Test
    void method_identifiers_throws_when_field_is_missing(
            @org.junit.jupiter.api.io.TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("no-mi.json");
        Files.writeString(file, "{\"abi\":[]}");

        RuntimeException error =
                assertThrows(
                        RuntimeException.class,
                        () -> AbiBase.methodIdentifiers(ContractAbiType.CUSTOM, file.toString()));
        assertEquals("ABI JSON does not contain methodIdentifiers: " + file, error.getMessage());
    }
}
