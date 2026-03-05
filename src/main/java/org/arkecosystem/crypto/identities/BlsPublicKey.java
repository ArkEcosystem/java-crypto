package org.arkecosystem.crypto.identities;

import org.arkecosystem.crypto.encoding.Hex;
import supranational.blst.P1_Affine;

public class BlsPublicKey {

    public static boolean validate(String publicKeyHex) {
        try {
            if (publicKeyHex.startsWith("0x")) {
                publicKeyHex = publicKeyHex.substring(2);
            }

            if (publicKeyHex.length() != 96) {
                return false;
            }

            byte[] publicKeyBytes = Hex.decode(publicKeyHex);

            P1_Affine point = new P1_Affine(publicKeyBytes);

            return point.in_group();
        } catch (Exception e) {
            return false;
        }
    }
}
