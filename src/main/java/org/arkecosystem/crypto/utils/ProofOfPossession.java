package org.arkecosystem.crypto.utils;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import org.arkecosystem.crypto.encoding.Hex;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import supranational.blst.P1;
import supranational.blst.P2;
import supranational.blst.SecretKey;

public class ProofOfPossession {

    private static final String POP_DST = "BLS_POP_BLS12381G2_XMD:SHA-256_SSWU_RO_POP_";

    public static final class Result {
        public final byte[] pk;
        public final byte[] pop;

        Result(byte[] pk, byte[] pop) {
            this.pk = pk;
            this.pop = pop;
        }
    }

    public static byte[] deriveBlsPrivateKey(String mnemonic) {
        return deriveChildSk(mnemonic).to_bendian();
    }

    public static String deriveBlsPublicKey(String mnemonic) {
        return Hex.encode(new P1(deriveChildSk(mnemonic)).compress());
    }

    public static Result buildProofOfPossession(byte[] secretKeyBytes) {
        SecretKey sk = new SecretKey();
        sk.from_bendian(secretKeyBytes);
        byte[] pk = new P1(sk).compress();
        P2 sig = new P2().hash_to(pk, POP_DST).sign_with(sk);
        return new Result(pk, sig.compress());
    }

    public static Result fromMnemonic(String mnemonic) {
        return buildProofOfPossession(deriveBlsPrivateKey(mnemonic));
    }

    private static SecretKey deriveChildSk(String mnemonic) {
        byte[] seed = mnemonicToSeed(mnemonic);
        SecretKey master = new SecretKey();
        master.derive_master_eip2333(seed);
        SecretKey child = new SecretKey();
        child.derive_child_eip2333(master, 0L);
        return child;
    }

    private static byte[] mnemonicToSeed(String mnemonic) {
        byte[] pass =
                Normalizer.normalize(mnemonic, Normalizer.Form.NFKD)
                        .getBytes(StandardCharsets.UTF_8);
        byte[] salt = "mnemonic".getBytes(StandardCharsets.UTF_8);
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
        gen.init(pass, salt, 2048);
        return ((KeyParameter) gen.generateDerivedParameters(512)).getKey();
    }


}
