package org.arkecosystem.crypto.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class UnitConverter {

    private static final BigDecimal WEI_MULTIPLIER = BigDecimal.ONE;
    private static final BigDecimal GWEI_MULTIPLIER = new BigDecimal("1000000000");
    private static final BigDecimal ARK_MULTIPLIER = new BigDecimal("1000000000000000000");

    private UnitConverter() {}

    public static BigDecimal parseUnits(String value) {
        return parseUnits(value, "ark");
    }

    public static BigDecimal parseUnits(String value, String unit) {
        BigDecimal multiplier = multiplierFor(unit);
        return new BigDecimal(value).multiply(multiplier).setScale(0, RoundingMode.DOWN);
    }

    public static BigDecimal formatUnits(String value) {
        return formatUnits(value, "ark");
    }

    public static BigDecimal formatUnits(String value, String unit) {
        BigDecimal amount = new BigDecimal(value);
        switch (unit.toLowerCase()) {
            case "wei":
                return strip(amount.divide(WEI_MULTIPLIER, 0, RoundingMode.HALF_UP));
            case "gwei":
                return strip(amount.divide(GWEI_MULTIPLIER, 9, RoundingMode.HALF_UP));
            case "ark":
                return strip(amount.divide(ARK_MULTIPLIER, 18, RoundingMode.HALF_UP));
            default:
                throw new IllegalArgumentException(unsupportedMessage(unit));
        }
    }

    public static String weiToArk(String value) {
        return weiToArk(value, null);
    }

    public static String weiToArk(String value, String suffix) {
        return convertToArk(value, "wei", suffix);
    }

    public static String gweiToArk(String value) {
        return gweiToArk(value, null);
    }

    public static String gweiToArk(String value, String suffix) {
        return convertToArk(value, "gwei", suffix);
    }

    private static String convertToArk(String value, String fromUnit, String suffix) {
        BigDecimal asWei = parseUnits(value, fromUnit);
        String converted = formatUnits(asWei.toPlainString(), "ark").toPlainString();
        return suffix == null ? converted : converted + " " + suffix;
    }

    private static BigDecimal multiplierFor(String unit) {
        switch (unit.toLowerCase()) {
            case "wei":
                return WEI_MULTIPLIER;
            case "gwei":
                return GWEI_MULTIPLIER;
            case "ark":
                return ARK_MULTIPLIER;
            default:
                throw new IllegalArgumentException(unsupportedMessage(unit));
        }
    }

    private static String unsupportedMessage(String unit) {
        return "Unsupported unit: " + unit + ". Supported units are 'wei', 'gwei', and 'ark'.";
    }

    private static BigDecimal strip(BigDecimal value) {
        BigDecimal stripped = value.stripTrailingZeros();
        if (stripped.scale() < 0) {
            stripped = stripped.setScale(0, RoundingMode.UNNECESSARY);
        }
        return stripped;
    }
}
