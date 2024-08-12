package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.Schnorr;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;
import org.miracl.core.BLS12381.BIG;
import org.miracl.core.BLS12381.ECP2;
import org.miracl.core.BLS12381.FP2;

public class ValidatorRegistrationBuilder
        extends AbstractTransactionBuilder<ValidatorRegistrationBuilder> {

    public ValidatorRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.VALIDATOR_REGISTRATION.getValue();
    }

    public ValidatorRegistrationBuilder publicKeyAsset(String publicKey) {
        validateBlsPublicKey(publicKey);
        this.transaction.asset.validatorPublicKey = publicKey;
        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new ValidatorRegistration();
    }

    @Override
    public ValidatorRegistrationBuilder instance() {
        return this;
    }

    private void validateBlsPublicKey(String publicKeyHex) {
        if (publicKeyHex.length() != 96) {
            throw new IllegalArgumentException("Invalid BLS public key length");
        }

        byte[] publicKeyBytes = Schnorr.hexStringToByteArray(publicKeyHex);

        // Log para verificar longitud de bytes
        System.out.println("publicKeyBytes length: " + publicKeyBytes.length);

        // Usar la función de la clase BLS para verificar si la clave pública es válida
        ECP2 publicKeyPoint = ECP2.fromBytes(publicKeyBytes);

        // Descomprimir la clave pública comprimida
        BIG xBig = BIG.fromBytes(publicKeyBytes);
        ECP2 publicKeyPoint = new ECP2(xBig, 0); // El segundo argumento es un bit de paridad

        // Verificar que el punto no esté en el infinito
        if (publicKeyPoint.is_infinity()) {
            throw new IllegalArgumentException("Invalid BLS public key: point at infinity");
        }

        // Verificar que el punto realmente pertenezca a la curva utilizando la ecuación de la curva
        FP2 rhs = ECP2.RHS(publicKeyPoint.getX()); // Right-hand side: y^2 = x^3 + Ax + B
        FP2 lhs = new FP2(publicKeyPoint.getY());
        lhs.sqr(); // Left-hand side: y^2

        if (!lhs.equals(rhs)) {
            throw new IllegalArgumentException(
                    "Invalid BLS public key: does not satisfy curve equation");
        }

        System.out.println("BLS public key is valid.");

        // Verificación usando PAIR.G2member
        // boolean isValid = PAIR.G2member(publicKeyPoint);
        // System.out.println("Is publicKeyPoint valid using PAIR.G2member: " + isValid);

        // // Alternativa: Usar BLS.core_verify para verificar la clave pública
        // // La clave pública W y un mensaje arbitrario se pueden usar aquí
        // byte[] dummyMessage = "test message".getBytes();
        // byte[] dummySignature = new byte[publicKeyBytes.length]; // Firma ficticia para validar
        // la clave

        // // Realizar la verificación
        // int result = BLS.core_verify(dummySignature, dummyMessage, publicKeyBytes);

        // if (result != BLS.BLS_OK) {
        //     throw new IllegalArgumentException("Invalid BLS public key according to
        // core_verify");
        // }

        // System.out.println("Public key is valid according to BLS.core_verify");

        // // Validar que el punto pertenece al grupo G2
        // boolean isValid = PAIR.G2member(publicKeyPoint);
        // // Depuración: Resultado de la validación
        // System.out.println("Is publicKeyPoint valid: " + isValid);

        // if (!isValid) {
        //     throw new IllegalArgumentException("Invalid BLS public key");
        // }
    }
}
