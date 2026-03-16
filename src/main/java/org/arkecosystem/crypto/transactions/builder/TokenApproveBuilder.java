package org.arkecosystem.crypto.transactions.builder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.EvmCall;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class TokenApproveBuilder extends AbstractTransactionBuilder<TokenApproveBuilder> {
    public TokenApproveBuilder contractAddress(String address) {
        this.transaction.recipientAddress = address;
        return this.instance();
    }

    public TokenApproveBuilder spender(String address, BigInteger amount) {
        List<Object> args = new ArrayList<>();
        args.add(address);
        args.add(amount);

        try {
            String payload =
                    new AbiEncoder("Abi.Token.json")
                            .encodeFunctionCall(AbiFunction.APPROVE.toString(), args);

            this.transaction.data = payload.replaceFirst("^0x", "");
        } catch (Exception e) {
            throw new RuntimeException("Error encoding token approve", e);
        }

        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new EvmCall();
    }

    @Override
    protected TokenApproveBuilder instance() {
        return this;
    }
}
