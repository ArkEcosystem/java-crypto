package org.arkecosystem.crypto.transactions.builder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.EvmCall;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class BatchTransferBuilder extends AbstractTransactionBuilder<BatchTransferBuilder> {
    private String tokenAddress;
    private final List<String> recipients = new ArrayList<>();
    private final List<BigInteger> amounts = new ArrayList<>();

    public BatchTransferBuilder() {
        super();
        this.transaction.recipientAddress = ContractAddresses.BATCH_TRANSFER.address();
    }

    public BatchTransferBuilder tokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
        return this.instance();
    }

    public BatchTransferBuilder addRecipient(String address, BigInteger amount) {
        this.recipients.add(address);
        this.amounts.add(amount);
        return this.instance();
    }

    @Override
    public BatchTransferBuilder sign(String passphrase) {
        this.encode();
        return super.sign(passphrase);
    }

    private void encode() {
        if (this.recipients.isEmpty()) {
            throw new RuntimeException("Must add at least one recipient before encoding.");
        }

        if (this.tokenAddress == null) {
            throw new RuntimeException("Must set tokenAddress before encoding.");
        }

        List<Object> args = new ArrayList<>();
        args.add(this.tokenAddress);
        args.add(new ArrayList<Object>(this.recipients));
        args.add(new ArrayList<Object>(this.amounts));

        try {
            String payload =
                    new AbiEncoder(ContractAbiType.ERC20BATCH_TRANSFER)
                            .encodeFunctionCall(AbiFunction.BATCH_TRANSFER_FROM.toString(), args);

            this.transaction.data = payload.replaceFirst("^0x", "");
        } catch (Exception e) {
            throw new RuntimeException("Error encoding batch transfer", e);
        }
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new EvmCall();
    }

    @Override
    protected BatchTransferBuilder instance() {
        return this;
    }
}
