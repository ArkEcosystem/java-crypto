package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ProofOfPossessionTest {

    // Pinned vectors from typescript-crypto/tests/unit/utils/ProofOfPossession.test.ts
    private static final String SK_A_HEX =
            "67d53f170b908cabb9eb326c3c337762d59289a8fec79f7bc9254b584b73265c";
    private static final String SK_A_PK =
            "a7e75af9dd4d868a41ad2f5a5b021d653e31084261724fb40ae2f1b1c31c778d3b9464502d599cf6720723ec5c68b59d";
    private static final String SK_A_POP =
            "878ad02e1f215d40722bd77a0148adb8dfaad4514157600a0a926cfc58589fa4e79d3d4d579cc4149237b8100efdcff110dd2a251c52543539d499c8f24b142da66d1dc19ec44b3d9c3f71112b2705e5557f932a36bd9cd9b3544ab0d9e6a677";

    private static final String PASSPHRASE_A =
            "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about";
    private static final String PASSPHRASE_A_PK =
            "b7e5e1ea87f5be0ac7d97402a73002d302a3672f58aacb443cc5456190eac50f439f61b73556209de7624dfb535459e8";

    private static final String PASSPHRASE_ZH =
            "逻 砖 浇 动 牌 霞 扎 团 柴 年 虽 类"
                    + " 因 什 电 骂 后 什 帽 玻 缩 像"
                    + " 壮 摘";
    private static final String PASSPHRASE_ZH_SK =
            "3c91a0142bdb0c777b64c74ae2a76cb110b1cdd56f2781aeb5ba7f1eac983b91";
    private static final String PASSPHRASE_ZH_PK =
            "a6fab215d09829188d9fe753ae0162cb0aacfc875a2246550585205cd95e062b79585402b3ef853a8afc73a13e09065b";
    private static final String PASSPHRASE_ZH_POP =
            "b2333783b03f6b0c095391c48b874a1d4e6beac5c840a5a89ef3954f94bf5519210578aa734ecbe90e90a7417eed001a01e3402e3c899183ffee6fe636a02833276b0f3bac543312804d2c9908233252e998696d6fd34df847137b48c4548b78";

    @Test
    public void buildProofOfPossessionPkMatchesPinnedVector() {
        ProofOfPossession.Result result = ProofOfPossession.buildProofOfPossession(fromHex(SK_A_HEX));
        assertEquals(SK_A_PK, toHex(result.pk));
    }

    @Test
    public void buildProofOfPossessionPopMatchesPinnedVector() {
        ProofOfPossession.Result result = ProofOfPossession.buildProofOfPossession(fromHex(SK_A_HEX));
        assertEquals(SK_A_POP, toHex(result.pop));
    }

    @Test
    public void fromMnemonicChineseMnemonicPkAndPop() {
        ProofOfPossession.Result result = ProofOfPossession.fromMnemonic(PASSPHRASE_ZH);
        assertEquals(PASSPHRASE_ZH_PK, toHex(result.pk));
        assertEquals(PASSPHRASE_ZH_POP, toHex(result.pop));
    }

    @Test
    public void deriveBlsPublicKeyMatchesPinnedVector() {
        assertEquals(PASSPHRASE_A_PK, ProofOfPossession.deriveBlsPublicKey(PASSPHRASE_A));
    }

    @Test
    public void deriveBlsPrivateKeyChineseMnemonic() {
        assertEquals(PASSPHRASE_ZH_SK, toHex(ProofOfPossession.deriveBlsPrivateKey(PASSPHRASE_ZH)));
    }

    @Test
    public void deriveBlsPublicKeyChineseMnemonic() {
        assertEquals(PASSPHRASE_ZH_PK, ProofOfPossession.deriveBlsPublicKey(PASSPHRASE_ZH));
    }

    @Test
    public void buildProofOfPossessionChineseMnemonicPkAndPop() {
        byte[] sk = ProofOfPossession.deriveBlsPrivateKey(PASSPHRASE_ZH);
        ProofOfPossession.Result result = ProofOfPossession.buildProofOfPossession(sk);
        assertEquals(PASSPHRASE_ZH_PK, toHex(result.pk));
        assertEquals(PASSPHRASE_ZH_POP, toHex(result.pop));
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] =
                    (byte)
                            ((Character.digit(hex.charAt(i), 16) << 4)
                                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
