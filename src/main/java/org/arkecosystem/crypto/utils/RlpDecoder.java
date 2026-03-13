package org.arkecosystem.crypto.utils;

import java.util.ArrayList;
import java.util.List;

public class RlpDecoder {

    public static List<byte[]> decode(byte[] input) {
        List<byte[]> result = new ArrayList<>();
        decodeList(input, 0, input.length, result);
        return result;
    }

    private static void decodeList(byte[] input, int offset, int end, List<byte[]> result) {
        if (offset >= end) return;

        int prefix = input[offset] & 0xFF;

        if (prefix <= 0x7F) {
            result.add(new byte[] {input[offset]});
            decodeList(input, offset + 1, end, result);
        } else if (prefix <= 0xB7) {
            int len = prefix - 0x80;
            byte[] data = new byte[len];
            System.arraycopy(input, offset + 1, data, 0, len);
            result.add(data);
            decodeList(input, offset + 1 + len, end, result);
        } else if (prefix <= 0xBF) {
            int lenOfLen = prefix - 0xB7;
            int len = readLength(input, offset + 1, lenOfLen);
            byte[] data = new byte[len];
            System.arraycopy(input, offset + 1 + lenOfLen, data, 0, len);
            result.add(data);
            decodeList(input, offset + 1 + lenOfLen + len, end, result);
        } else if (prefix <= 0xF7) {
            int len = prefix - 0xC0;
            List<byte[]> inner = new ArrayList<>();
            decodeList(input, offset + 1, offset + 1 + len, inner);
            result.addAll(inner);
            decodeList(input, offset + 1 + len, end, result);
        } else {
            int lenOfLen = prefix - 0xF7;
            int len = readLength(input, offset + 1, lenOfLen);
            List<byte[]> inner = new ArrayList<>();
            decodeList(input, offset + 1 + lenOfLen, offset + 1 + lenOfLen + len, inner);
            result.addAll(inner);
            decodeList(input, offset + 1 + lenOfLen + len, end, result);
        }
    }

    private static int readLength(byte[] input, int offset, int lenOfLen) {
        int len = 0;
        for (int i = 0; i < lenOfLen; i++) {
            len = (len << 8) | (input[offset + i] & 0xFF);
        }
        return len;
    }
}
