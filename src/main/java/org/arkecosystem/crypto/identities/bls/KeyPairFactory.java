package org.arkecosystem.crypto.identities.bls;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import com.herumi.mcl.G1;
import com.herumi.mcl.G2;
import com.herumi.mcl.Mcl;
import com.herumi.mcl.MclConstants;
import com.herumi.mcl.Fr;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.util.encoders.Hex;
import org.nightcode.bip39.Bip39;
import org.nightcode.bip39.Bip39Exception;
import org.nightcode.bip39.dictionary.EnglishDictionary;
import org.arkecosystem.crypto.signature.bls.Bls;
import org.arkecosystem.crypto.signature.bls.BlsConstants;
import org.arkecosystem.crypto.signature.bls.JNIEnv;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import org.bitcoinj.core.Sha256Hash;

public class KeyPairFactory {

    static {
        String lib = "mcljava";
        JNIEnv env = new JNIEnv();
        env.prepare();
        System.loadLibrary(lib);
    }
    private static final BigInteger BLS_R = new BigInteger("52435875175126190479447740508185965837690552500527637822603658699938581184513");

    public KeyPairFactory() {
        Mcl.SystemInit(MclConstants.BLS12_381);
    }

    public KeyPair fromPassphrase(String passphrase) {
        Bip39 bip39 = new Bip39(EnglishDictionary.instance());

        try {
            byte[] seed = bip39.createSeed(passphrase, "");

            byte[] masterKey = deriveMaster(seed);

            byte[] privateKey = deriveChild(masterKey, BigInteger.ZERO);

            return fromPrivateKey(privateKey);
        } catch (Bip39Exception e) {
            throw new RuntimeException("Failed to generate seed from passphrase", e);
        }
    }

    public static byte[] deriveMaster(byte[] seed) {
        return hkdfModR(seed);
    }

    public static byte[] deriveChild(byte[] parentKey, BigInteger index) {
        try {
            byte[] salt = i2osp(index, 4);

            byte[] lamportPK = parentSKToLamportPK(parentKey, salt);

            return hkdfModR(lamportPK);
        } catch (Exception e) {
            throw new RuntimeException("Failed to derive child key", e);
        }
    }

    private static byte[] parentSKToLamportPK(byte[] parentSK, byte[] index) {
        byte[][] lamport0 = ikmToLamportSK(parentSK, index);
        byte[] notParentSK = invertBytes(parentSK);
        byte[][] lamport1 = ikmToLamportSK(notParentSK, index);

        byte[][] hashedParts = new byte[lamport0.length + lamport1.length][32];
        
        for (int i = 0; i < lamport0.length; i++) {
            hashedParts[i] = Sha256Hash.hash(lamport0[i]);
        }

        for (int i = 0; i < lamport1.length; i++) {
            hashedParts[lamport0.length + i] = Sha256Hash.hash(lamport1[i]);
        }

        byte[] concatenated = concatArrays(hashedParts);

        return Sha256Hash.hash(concatenated); 
    }

    private static byte[][] ikmToLamportSK(byte[] ikm, byte[] salt) {
        HKDFParameters hkdfParams = new HKDFParameters(ikm, salt, null);
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(hkdfParams);

        byte[] okm = new byte[32 * 255];
        hkdf.generateBytes(okm, 0, okm.length);

        byte[][] sk = new byte[255][32];
        for (int i = 0; i < 255; i++) {
            System.arraycopy(okm, i * 32, sk[i], 0, 32);
        }
        return sk;
    }

    private static byte[] hkdfModR(byte[] ikm) {
        try {
            byte[] salt = "BLS-SIG-KEYGEN-SALT-".getBytes(StandardCharsets.UTF_8);
            
            byte[] input = concat(ikm, new byte[]{0x00});
            
            byte[] label = new byte[]{0x00, 0x30};

            byte[] okm = null;
            
            BigInteger SK = BigInteger.ZERO;

            while (SK.equals(BigInteger.ZERO)) {
                salt = Sha256Hash.hash(salt);
                
                HKDFParameters hkdfParams = new HKDFParameters(input, salt, label);
                HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
                hkdf.init(hkdfParams);

                okm = new byte[48];
                hkdf.generateBytes(okm, 0, okm.length);
                
                SK = new BigInteger(1, okm).mod(BLS_R);
            }

            return i2osp(SK, 32);
        } catch (Exception e) {
            throw new RuntimeException("Failed to derive master key", e);
        }
    }

    private static byte[] invertBytes(byte[] bytes) {
        byte[] inverted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            inverted[i] = (byte) ~bytes[i];
        }
        return inverted;
    }

    private static byte[] concatArrays(byte[][] arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }


    private static byte[] i2osp(BigInteger value, int length) {
        byte[] result = new byte[length];
        byte[] bytes = value.toByteArray();
        int srcPos = Math.max(0, bytes.length - length);
        int destPos = Math.max(0, length - bytes.length);
        int len = Math.min(bytes.length, length);
        System.arraycopy(bytes, srcPos, result, destPos, len);
        return result;
    }

    private static byte[] concat(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public KeyPair fromPrivateKey(byte[] privateKeyBytes) {
        if (privateKeyBytes.length != 32) {
            throw new IllegalArgumentException("Invalid private key length");
        }

        // Convertir el array de bytes en un elemento Fr (clave privada)
        Fr privateKey = new Fr();
        privateKey.setLittleEndianMod(privateKeyBytes);
        
        // Generar la clave pública utilizando directamente el punto base G1 predefinido
        G1 publicKey = new G1();

        G1 Q = new G1();
        Q.setStr(Bls.BaseG1);

        Mcl.mul(publicKey, Q, privateKey);

        // Serializar la clave pública a un array de bytes
        byte[] publicKeyBytes = publicKey.serialize();

        // Convertir las claves privadas y públicas a cadenas hexadecimales
        String privateKeyHex = bytesToHex(privateKeyBytes);
        String publicKeyHex = bytesToHex(publicKeyBytes);

        System.out.println("Private key: " + privateKeyHex);
        System.out.println("Public key: " + publicKeyHex);
        System.out.println("Public key1 b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93");

        return new KeyPair(privateKeyHex, publicKeyHex);
    }

    public static class KeyPair {
        private final String privateKey;
        private final String publicKey;

        public KeyPair(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }
    }
}