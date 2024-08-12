package org.arkecosystem.crypto.identities;

import com.herumi.mcl.Fr;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.arkecosystem.crypto.signature.bls.Bls;
import org.arkecosystem.crypto.signature.bls.BlsConstants;
import org.bitcoinj.crypto.MnemonicCode;
import org.miracl.core.BLS12381.BLS;

public class BlsKeypair {

    public static BlsKeyPair fromPassphrase(String passphrase) {
        Bls bls = new Bls(BlsConstants.BLS12_381);

        // Paso 1: Convertir el passphrase en una lista de palabras (mnemonic)
        List<String> mnemonic = Arrays.asList(passphrase.split(" "));

        // Paso 2: Generar la semilla usando BIP39
        byte[] seed = MnemonicCode.toSeed(mnemonic, "");
        System.out.println("Seed: " + bytesToHex(seed)); // Debug output

        // Paso 3: Derivar la clave maestra desde la semilla (equivalente a deriveMaster)
        // Fr masterKeyFr = new Fr();
        // masterKeyFr.setLittleEndianMod(seed);
        byte[] masterKeyBytes = deriveMasterKey(seed);
        System.out.println("Master Key: " + bytesToHex(masterKeyBytes)); // Debug output

        // Paso 4: Derivar la clave hija desde la clave maestra (equivalente a deriveChild)
        // Usamos masterKeyBytes como semilla para derivar la clave hija
        // Fr childKeyFr = new Fr();
        // childKeyFr.setLittleEndianMod(deriveChildKey(masterKeyBytes)); // Deriva la clave hija
        // correctamente
        // byte[] privateKeyBytes = childKeyFr.serialize();
        byte[] privateKeyBytes = deriveMasterKey(seed);
        System.out.println(
                "Child Key (Private Key): " + bytesToHex(privateKeyBytes)); // Debug output

        // Generar la clave pública correspondiente
        // G1 publicKeyG1 = new G1();
        // G1 Q = new G1();
        // Q.setStr(BlsConstants.BaseG1);
        // Mcl.mul(publicKeyG1, Q, childKeyFr);
        // byte[] publicKeyBytes = publicKeyG1.serialize();
        // System.out.println("Public Key: " + bytesToHex(publicKeyBytes)); // Debug output
        byte[] publicKeyBytes = deriveMasterKey(seed);

        return new BlsKeyPair(true, bytesToHex(privateKeyBytes), bytesToHex(publicKeyBytes));
    }

    private static byte[] hashPassphrase(String passphrase) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(passphrase.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate seed from passphrase", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] deriveMasterKey(byte[] seed) {
        byte[] privateKey = new byte[BLS.BGS];
        byte[] publicKey = new byte[BLS.BFS];

        // Utilizar MIRACL Core BLS para derivar el par de claves
        BLS.KeyPairGenerate(seed, privateKey, publicKey);

        // Retornamos la clave privada (master key)
        return privateKey;

        // // Usar HKDF con SHA-256 para derivar la clave maestra
        // HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        // byte[] salt = "BLS-SIG-KEYGEN-SALT-".getBytes();  // Sal basado en EIP-2333
        // byte[] info = new byte[0];  // No información adicional
        // hkdf.init(new HKDFParameters(seed, salt, info));
        // byte[] okm = new byte[32];
        // hkdf.generateBytes(okm, 0, okm.length);
        // return okm;
    }

    private static byte[] deriveChildKey(byte[] masterKeyBytes, int index) {
        // Derivar la clave hija simplemente usando la clave maestra
        Fr childKeyFr = new Fr();
        childKeyFr.setLittleEndianMod(masterKeyBytes); // Deriva la clave hija
        return childKeyFr.serialize();
    }

    public static class BlsKeyPair {
        public final boolean compressed;
        public final String privateKey;
        public final String publicKey;

        public BlsKeyPair(boolean compressed, String privateKey, String publicKey) {
            this.compressed = compressed;
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        @Override
        public String toString() {
            return "BlsKeyPair{"
                    + "compressed="
                    + compressed
                    + ", privateKey='"
                    + privateKey
                    + '\''
                    + ", publicKey='"
                    + publicKey
                    + '\''
                    + '}';
        }
    }
}
