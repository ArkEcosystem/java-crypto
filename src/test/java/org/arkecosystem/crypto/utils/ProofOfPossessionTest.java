package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.exceptions.InvalidProofOfPossessionException;
import org.arkecosystem.crypto.networks.INetwork;
import org.arkecosystem.crypto.networks.Testnet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import supranational.blst.BLST_ERROR;
import supranational.blst.P1_Affine;
import supranational.blst.P2_Affine;

public class ProofOfPossessionTest {

    private static final String POP_DST = "MAINSAIL_BLS_POP_BLS12381G2_XMD:SHA-256_SSWU_RO_POP_";

    private static final String SK_A_HEX =
            "67d53f170b908cabb9eb326c3c337762d59289a8fec79f7bc9254b584b73265c";
    private static final String SK_A_PK =
            "a7e75af9dd4d868a41ad2f5a5b021d653e31084261724fb40ae2f1b1c31c778d3b9464502d599cf6720723ec5c68b59d";

    private static final String REGISTRANT_ADDRESS = "0x75545540230d5c3BEf023202d23CB74cFA723376";

    // Mainsail's reference vector (packages/crypto-key-pair-bls12-381), bound to chainId 10_000.
    private static final int MAINSAIL_CHAIN_ID = 10_000;
    private static final String SK_A_MAINSAIL_POP =
            "a892e94d8ed6d0fe8792dcb31b7c5116a7d138ad4bbbd044780a7c314e86673e783850121dc34d0edfa2a2560c2f30a402f4fa5106ff71d5c69bc3027210ef90b3d3ae0a19ffc9f554b37aca72f3bb25788c3177514d94e041441ba9d029b3ba";

    private static final String PASSPHRASE_A =
            "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about";
    private static final String PASSPHRASE_A_PK =
            "b7e5e1ea87f5be0ac7d97402a73002d302a3672f58aacb443cc5456190eac50f439f61b73556209de7624dfb535459e8";

    private static final String PASSPHRASE_ZH =
            "逻 砖 浇 动 牌 霞 扎 团 柴 年 虽 类" + " 因 什 电 骂 后 什 帽 玻 缩 像" + " 壮 摘";
    private static final String PASSPHRASE_ZH_SK =
            "3c91a0142bdb0c777b64c74ae2a76cb110b1cdd56f2781aeb5ba7f1eac983b91";
    private static final String PASSPHRASE_ZH_PK =
            "a6fab215d09829188d9fe753ae0162cb0aacfc875a2246550585205cd95e062b79585402b3ef853a8afc73a13e09065b";

    private static byte[] message(long chainId, String registrantAddress, byte[] pk) {
        byte[] chainIdBytes = BigInteger.valueOf(chainId).toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(32 + 20 + pk.length);
        buffer.position(32 - chainIdBytes.length);
        buffer.put(chainIdBytes);
        buffer.put(Hex.decode(registrantAddress.substring(2).toLowerCase()));
        buffer.put(pk);
        return buffer.array();
    }

    private static boolean verifies(ProofOfPossession.Result result, byte[] message) {
        return new P2_Affine(result.pop)
                        .core_verify(new P1_Affine(result.pk), true, message, POP_DST)
                == BLST_ERROR.BLST_SUCCESS;
    }

    @Test
    public void bindsThePopToTheConfiguredChainIdAndRegistrantAddress() {
        ProofOfPossession.Result result =
                ProofOfPossession.buildProofOfPossession(Hex.decode(SK_A_HEX), REGISTRANT_ADDRESS);
        int chainId = Network.get().chainId();

        assertEquals(true, verifies(result, message(chainId, REGISTRANT_ADDRESS, result.pk)));
        assertEquals(false, verifies(result, message(chainId + 1, REGISTRANT_ADDRESS, result.pk)));
        assertEquals(
                false,
                verifies(
                        result,
                        message(chainId, "0xBd6F65c58A46427AF4B257cBE231D0eD69eD5508", result.pk)));
    }

    @Test
    public void throwsOnAnInvalidRegistrantAddress() {
        byte[] sk = Hex.decode(SK_A_HEX);

        assertThrows(
                InvalidProofOfPossessionException.class,
                () -> ProofOfPossession.buildProofOfPossession(sk, "0x1234"));

        // A mistyped checksum must not silently bind the PoP to a different address.
        assertThrows(
                InvalidProofOfPossessionException.class,
                () ->
                        ProofOfPossession.buildProofOfPossession(
                                sk, "0x75545540230d5c3bEf023202d23CB74cFA723376"));
    }

    private static <T> T withChainId(int chainId, Supplier<T> action) {
        INetwork previous = Network.get();
        Network.set(
                new Testnet() {
                    @Override
                    public int chainId() {
                        return chainId;
                    }
                });

        try {
            return action.get();
        } finally {
            Network.set(previous);
        }
    }

    @Test
    public void matchesMainsailPinnedTestVectorForSkA() {
        ProofOfPossession.Result result =
                withChainId(
                        MAINSAIL_CHAIN_ID,
                        () ->
                                ProofOfPossession.buildProofOfPossession(
                                        Hex.decode(SK_A_HEX), REGISTRANT_ADDRESS));

        assertEquals(SK_A_PK, Hex.encode(result.pk));
        assertEquals(SK_A_MAINSAIL_POP, Hex.encode(result.pop));
    }

    @Test
    public void deriveBlsPublicKeyMatchesPinnedVector() {
        assertEquals(PASSPHRASE_A_PK, ProofOfPossession.deriveBlsPublicKey(PASSPHRASE_A));
    }

    @Test
    public void deriveBlsPrivateKeyChineseMnemonic() {
        assertEquals(
                PASSPHRASE_ZH_SK, Hex.encode(ProofOfPossession.deriveBlsPrivateKey(PASSPHRASE_ZH)));
    }

    @Test
    public void deriveBlsPublicKeyChineseMnemonic() {
        assertEquals(PASSPHRASE_ZH_PK, ProofOfPossession.deriveBlsPublicKey(PASSPHRASE_ZH));
    }

    record BlsKeyVector(
            String language,
            int index,
            String mnemonic,
            String sk,
            String pk,
            int chainId,
            String address,
            String pop) {
        @Override
        public String toString() {
            return language + "[" + index + "]";
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("blsKeyVectors")
    public void blsKeyVectorsMatchJsonDataset(BlsKeyVector v) {
        assertEquals(v.sk(), Hex.encode(ProofOfPossession.deriveBlsPrivateKey(v.mnemonic())));
        assertEquals(v.pk(), ProofOfPossession.deriveBlsPublicKey(v.mnemonic()));

        ProofOfPossession.Result result =
                withChainId(
                        v.chainId(),
                        () -> ProofOfPossession.fromMnemonic(v.mnemonic(), v.address()));
        assertEquals(v.pk(), Hex.encode(result.pk));
        assertEquals(v.pop(), Hex.encode(result.pop));
    }

    static Stream<BlsKeyVector> blsKeyVectors() throws Exception {
        Type type = new TypeToken<Map<String, List<Map<String, String>>>>() {}.getType();
        Map<String, List<Map<String, String>>> data;
        try (InputStreamReader reader =
                new InputStreamReader(
                        ProofOfPossessionTest.class.getResourceAsStream("/bls-keys.json"))) {
            data = new Gson().fromJson(reader, type);
        }
        List<BlsKeyVector> vectors = new ArrayList<>();
        for (var entry : data.entrySet()) {
            int i = 1;
            for (var v : entry.getValue()) {
                vectors.add(
                        new BlsKeyVector(
                                entry.getKey(),
                                i++,
                                v.get("mnemonic"),
                                v.get("validatorPrivateKey"),
                                v.get("validatorPublicKey").substring(2),
                                Integer.parseInt(v.get("chainId")),
                                v.get("address"),
                                v.get("validatorPop").substring(2)));
            }
        }
        return vectors.stream();
    }
}
