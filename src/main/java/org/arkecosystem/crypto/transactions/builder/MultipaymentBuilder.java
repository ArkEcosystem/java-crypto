package org.arkecosystem.crypto.transactions.builder;

import java.math.BigInteger;
import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Multipayment;

public class MultipaymentBuilder extends AbstractTransactionBuilder<MultipaymentBuilder> {

    public MultipaymentBuilder() {
        super();
        this.transaction.recipientAddress = ContractAddresses.MULTIPAYMENT.address();
    }

    public MultipaymentBuilder pay(String address, BigInteger amount) {
        this.transaction.multipaymentRecipients.add(address);
        this.transaction.multipaymentAmounts.add(amount);

        BigInteger currentValue =
                this.transaction.value == null || this.transaction.value.isEmpty()
                        ? BigInteger.ZERO
                        : new BigInteger(this.transaction.value);
        this.transaction.value = currentValue.add(amount).toString();

        this.transaction.refreshPayloadData();

        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new Multipayment();
    }

    @Override
    protected MultipaymentBuilder instance() {
        return this;
    }
}
