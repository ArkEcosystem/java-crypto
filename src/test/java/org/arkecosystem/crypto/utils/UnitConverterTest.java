package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class UnitConverterTest {

    @Test
    void it_should_parse_units_into_wei() {
        assertEquals("1", UnitConverter.parseUnits("1", "wei").toPlainString());
    }

    @Test
    void it_should_parse_units_into_gwei() {
        assertEquals("1000000000", UnitConverter.parseUnits("1", "gwei").toPlainString());
    }

    @Test
    void it_should_parse_units_into_ark() {
        assertEquals("1000000000000000000", UnitConverter.parseUnits("1", "ark").toPlainString());
    }

    @Test
    void it_should_default_to_ark_when_no_unit_is_provided() {
        assertEquals("1000000000000000000", UnitConverter.parseUnits("1").toPlainString());
        assertEquals(
                "1000000000000000000",
                UnitConverter.formatUnits("1000000000000000000000000000000000000").toPlainString());
    }

    @Test
    void it_should_parse_decimal_units_into_ark() {
        assertEquals("100000000000000000", UnitConverter.parseUnits("0.1", "ark").toPlainString());
    }

    @Test
    void it_should_truncate_fractional_wei_when_parsing() {
        assertEquals("1", UnitConverter.parseUnits("1.9", "wei").toPlainString());
    }

    @Test
    void it_should_match_unit_names_case_insensitively() {
        assertEquals("1000000000000000000", UnitConverter.parseUnits("1", "ARK").toPlainString());
        assertEquals("1", UnitConverter.formatUnits("1000000000", "GWEI").toPlainString());
    }

    @Test
    void it_should_format_units_from_wei() {
        String[][] cases = {
            {"1", "1"},
            {"10", "10"},
            {"100", "100"},
            {"1000", "1000"},
            {"10000", "10000"}
        };

        for (String[] pair : cases) {
            assertEquals(pair[1], UnitConverter.formatUnits(pair[0], "wei").toPlainString());
        }
    }

    @Test
    void it_should_format_units_from_gwei() {
        String[][] cases = {
            {"100000001", "0.100000001"},
            {"100000000", "0.1"},
            {"1000000000", "1"},
            {"10000000000", "10"},
            {"100000000000", "100"},
            {"1000000000000", "1000"},
            {"10000000000000", "10000"}
        };

        for (String[] pair : cases) {
            assertEquals(pair[1], UnitConverter.formatUnits(pair[0], "gwei").toPlainString());
        }
    }

    @Test
    void it_should_format_units_from_ark() {
        String[][] cases = {
            {"100000000000000001", "0.100000000000000001"},
            {"100000000000000000", "0.1"},
            {"1000000000000000000", "1"},
            {"10000000000000000000", "10"},
            {"100000000000000000000", "100"},
            {"1000000000000000000000", "1000"},
            {"10000000000000000000000", "10000"}
        };

        for (String[] pair : cases) {
            assertEquals(pair[1], UnitConverter.formatUnits(pair[0], "ark").toPlainString());
        }
    }

    @Test
    void it_should_throw_for_unsupported_unit_in_parse() {
        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> UnitConverter.parseUnits("1", "unsupported"));
        assertEquals(
                "Unsupported unit: unsupported. Supported units are 'wei', 'gwei', and 'ark'.",
                thrown.getMessage());
    }

    @Test
    void it_should_throw_for_unsupported_unit_in_format() {
        assertThrows(
                IllegalArgumentException.class,
                () -> UnitConverter.formatUnits("1", "unsupported"));
    }

    @Test
    void it_should_convert_wei_to_ark() {
        assertEquals("0.000000000000000001 DARK", UnitConverter.weiToArk("1", "DARK"));
        assertEquals("0.000000000000000001", UnitConverter.weiToArk("1"));
        assertEquals("1 DARK", UnitConverter.weiToArk("1000000000000000000", "DARK"));
        assertEquals("1", UnitConverter.weiToArk("1000000000000000000"));
    }

    @Test
    void it_should_convert_gwei_to_ark() {
        assertEquals("0.000000001 DARK", UnitConverter.gweiToArk("1", "DARK"));
        assertEquals("0.000000001", UnitConverter.gweiToArk("1"));
        assertEquals("1 DARK", UnitConverter.gweiToArk("1000000000", "DARK"));
        assertEquals("1", UnitConverter.gweiToArk("1000000000"));
    }

    @Test
    void it_should_return_bigdecimal_from_parse_and_format() {
        BigDecimal parsed = UnitConverter.parseUnits("1", "ark");
        BigDecimal formatted = UnitConverter.formatUnits("1000000000000000000", "ark");

        assertEquals(0, parsed.compareTo(new BigDecimal("1000000000000000000")));
        assertEquals(0, formatted.compareTo(BigDecimal.ONE));
    }

    @Test
    void it_should_preserve_precision_for_large_wei_values() {
        String hugeWei = "123456789012345678901234567890";
        BigDecimal asArk = UnitConverter.formatUnits(hugeWei, "ark");

        assertEquals("123456789012.34567890123456789", asArk.toPlainString());
    }
}
