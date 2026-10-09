package org.arkecosystem.crypto.utils;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.exceptions.InvalidProofOfPossessionException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;
import supranational.blst.P1;
import supranational.blst.P2;
import supranational.blst.SecretKey;

public class ProofOfPossession {

    private static final String POP_DST = "MAINSAIL_BLS_POP_BLS12381G2_XMD:SHA-256_SSWU_RO_POP_";

    public static final class Result {
        public final byte[] pk;
        public final byte[] pop;

        Result(byte[] pk, byte[] pop) {
            this.pk = pk;
            this.pop = pop;
        }
    }

    public static byte[] deriveBlsPrivateKey(String passphrase) {
        return deriveChildSk(passphrase).to_bendian();
    }

    public static String deriveBlsPublicKey(String passphrase) {
        return Hex.encode(new P1(deriveChildSk(passphrase)).compress());
    }

    public static Result buildProofOfPossession(byte[] secretKeyBytes, String registrantAddress) {
        if (!Address.validate(registrantAddress)) {
            throw new InvalidProofOfPossessionException(
                    "registrantAddress must be a valid address. Got " + registrantAddress + ".");
        }

        SecretKey sk = new SecretKey();
        sk.from_bendian(secretKeyBytes);

        // 1. Derive the compressed public key (48-byte G1).
        byte[] pk = new P1(sk).compress();

        // 2. Hash chainId ‖ registrantAddress ‖ pk to a G2 point under POP_DST, binding
        //    the proof to the registrant and chain.
        String packedChainId = TypeEncoder.encodePacked(new Uint256(Network.get().chainId()));
        String packedAddress =
                TypeEncoder.encodePacked(new org.web3j.abi.datatypes.Address(registrantAddress));
        String packedPk = TypeEncoder.encodePacked(new DynamicBytes(pk));
        byte[] message = Hex.decode(packedChainId + packedAddress + packedPk);

        // 3. Sign the hashed G2 point with the secret key.
        P2 sig = new P2().hash_to(message, POP_DST).sign_with(sk);
        return new Result(pk, sig.compress());
    }

    public static Result fromMnemonic(String passphrase, String registrantAddress) {
        return buildProofOfPossession(deriveBlsPrivateKey(passphrase), registrantAddress);
    }

    private static SecretKey deriveChildSk(String passphrase) {
        byte[] seed = passphraseToSeed(passphrase);
        SecretKey master = new SecretKey();
        master.derive_master_eip2333(seed);
        SecretKey child = new SecretKey();
        child.derive_child_eip2333(master, 0L);
        return child;
    }

    private static byte[] passphraseToSeed(String passphrase) {
        byte[] pass =
                Normalizer.normalize(passphrase, Normalizer.Form.NFKD)
                        .getBytes(StandardCharsets.UTF_8);
        byte[] salt = "mnemonic".getBytes(StandardCharsets.UTF_8);
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
        gen.init(pass, salt, 2048);
        return ((KeyParameter) gen.generateDerivedParameters(512)).getKey();
    }
}
