package org.arkecosystem.crypto;

import java.math.BigInteger;
import org.bitcoinj.core.Sha256Hash;

/** Heavily inspired in https://github.com/miketwk/bip-schnorr-java/blob/master/Schnorr.java */
public class Schnorr {
    public static final BigInteger p =
            new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
    public static final BigInteger n =
            new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
    public static final BigInteger[] G = {
        new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
        new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16)
    };

    public static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static BigInteger[] addPoint(BigInteger[] p1, BigInteger[] p2) {
        if (p1 == null || p1.length != 2) return p2;

        if (p2 == null || p2.length != 2) return p1;

        if (p1[0].compareTo(p2[0]) == 0 && p1[1].compareTo(p2[1]) != 0) return null;

        BigInteger lam;
        if (p1[0].compareTo(p2[0]) == 0 && p1[1].compareTo(p2[1]) == 0)
            lam =
                    (THREE.multiply(p1[0])
                                    .multiply(p1[0])
                                    .multiply(TWO.multiply(p1[1]).modPow(p.subtract(TWO), p)))
                            .mod(p);
        else
            lam =
                    (p2[1]
                                    .subtract(p1[1])
                                    .multiply(p2[0].subtract(p1[0]).modPow(p.subtract(TWO), p)))
                            .mod(p);

        BigInteger x3 = (lam.multiply(lam).subtract(p1[0]).subtract(p2[0])).mod(p);

        return new BigInteger[] {x3, lam.multiply(p1[0].subtract(x3)).subtract(p1[1]).mod(p)};
    }

    public static BigInteger[] multiplyPoint(BigInteger[] P, BigInteger n) {
        BigInteger[] R = null;
        for (int i = 0; i < 256; i++) {
            if (BigInteger.ONE.compareTo(n.shiftRight(i).and(BigInteger.ONE)) == 0)
                R = addPoint(R, P);
            P = addPoint(P, P);
        }
        return R;
    }

    public static BigInteger jacobi(BigInteger x) {
        return x.modPow(p.subtract(BigInteger.ONE).divide(TWO), p);
    }

    public static BigInteger[] bytesToPoint(byte[] b) {
        if (b[0] != 2 && b[0] != 3) return null;

        BigInteger odd = b[0] == 3 ? BigInteger.ONE : BigInteger.ZERO;
        BigInteger x = toBigInteger(b, 1, 32);
        BigInteger y_sq = x.modPow(THREE, p).add(BigInteger.valueOf(7)).mod(p);
        BigInteger y0 = y_sq.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
        if (y_sq.compareTo(y0.modPow(TWO, p)) != 0) return null;

        BigInteger y = y0.and(BigInteger.ONE).compareTo(odd) != 0 ? p.subtract(y0) : y0;

        return new BigInteger[] {x, y};
    }

    public static byte[] to32BytesData(BigInteger num) {
        String hexNum = num.toString(16);
        if (hexNum.length() < 64) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 64 - hexNum.length(); i++) sb.append("0");

            hexNum = sb.append(hexNum).toString();
        }
        return hexStringToByteArray(hexNum);
    }

    public static BigInteger toBigInteger(byte[] data, int startPos, int len) {
        return new BigInteger(bytesToHex(data, startPos, len), 16);
    }

    public static BigInteger toBigInteger(byte[] data) {
        return new BigInteger(bytesToHex(data), 16);
    }

    public static byte[] pointToBytes(BigInteger[] point) {
        byte[] res = new byte[33];
        res[0] =
                BigInteger.ONE.compareTo(point[1].and(BigInteger.ONE)) == 0
                        ? (byte) 0x03
                        : (byte) 0x02;
        System.arraycopy(to32BytesData(point[0]), 0, res, 1, 32);
        return res;
    }

    public static byte[] schnorrSign(byte[] msg, BigInteger seckey) {
        if (msg.length != 32) throw new RuntimeException("The message must be a 32-byte array.");

        if (BigInteger.ZERO.compareTo(seckey) > 0
                || seckey.compareTo(n.subtract(BigInteger.ONE)) > 0)
            throw new RuntimeException("The secret key must be an integer in the range 1..n-1.");

        byte[] resultData = new byte[32 + msg.length];
        System.arraycopy(to32BytesData(seckey), 0, resultData, 0, 32);
        System.arraycopy(msg, 0, resultData, 32, msg.length);

        BigInteger k0 = toBigInteger(Sha256Hash.hash(resultData)).mod(n);
        if (BigInteger.ZERO.compareTo(k0) == 0)
            throw new RuntimeException("Failure. This happens only with negligible probability.");

        BigInteger[] R = multiplyPoint(G, k0);

        BigInteger k = BigInteger.ONE.compareTo(jacobi(R[1])) != 0 ? n.subtract(k0) : k0;
        byte[] R0Bytes = to32BytesData(R[0]);
        byte[] eData = new byte[32 + 33 + 32];
        System.arraycopy(R0Bytes, 0, eData, 0, 32);
        System.arraycopy(pointToBytes(multiplyPoint(G, seckey)), 0, eData, 32, 33);
        System.arraycopy(msg, 0, eData, 65, 32);
        eData = Sha256Hash.hash(eData);
        BigInteger e = toBigInteger(eData).mod(n);

        byte[] finalData = new byte[64];
        System.arraycopy(R0Bytes, 0, finalData, 0, 32);
        System.arraycopy(to32BytesData(e.multiply(seckey).add(k).mod(n)), 0, finalData, 32, 32);

        return finalData;
    }

    public static boolean schnorrVerify(byte[] msg, byte[] pubkey, byte[] sig) {
        if (msg.length != 32) throw new RuntimeException("The message must be a 32-byte array.");

        if (pubkey.length != 33)
            throw new RuntimeException("The public key must be a 33-byte array.");

        if (sig.length != 64) throw new RuntimeException("The signature must be a 64-byte array.");

        BigInteger[] P = bytesToPoint(pubkey);
        if (P == null) return false;

        BigInteger r = toBigInteger(sig, 0, 32);
        BigInteger s = toBigInteger(sig, 32, 32);

        if (r.compareTo(p) >= 0 || s.compareTo(n) >= 0) return false;

        byte[] eData = new byte[32 + 33 + 32];
        System.arraycopy(sig, 0, eData, 0, 32);
        System.arraycopy(pointToBytes(P), 0, eData, 32, 33);
        System.arraycopy(msg, 0, eData, 65, 32);
        eData = Sha256Hash.hash(eData);
        BigInteger e = toBigInteger(eData).mod(n);

        BigInteger[] R = addPoint(multiplyPoint(G, s), multiplyPoint(P, n.subtract(e)));

        return R != null && BigInteger.ONE.compareTo(jacobi(R[1])) == 0 && r.compareTo(R[0]) == 0;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] =
                    (byte)
                            ((Character.digit(s.charAt(i), 16) << 4)
                                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte[] bytes, int startPos, int len) {
        char[] hexChars = new char[len * 2];
        for (int j = 0, i = startPos; j < len; j++, i++) {
            int v = bytes[i] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
