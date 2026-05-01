package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.UsernameRegistration;

public class UsernameRegistrationBuilder
        extends AbstractTransactionBuilder<UsernameRegistrationBuilder> {

    public UsernameRegistrationBuilder() {
        super();
        this.transaction.recipientAddress = ContractAddresses.USERNAMES.address();
    }

    public UsernameRegistrationBuilder username(String username) {
        validateUsername(username);

        this.transaction.username = username;
        this.transaction.refreshPayloadData();

        return this.instance();
    }

    private static void validateUsername(String username) {
        if (username == null || username.isEmpty() || username.length() > 20) {
            throw new IllegalArgumentException(
                    "Username must be between 1 and 20 characters long. Got "
                            + (username == null ? 0 : username.length())
                            + " characters.");
        }

        if (!username.matches("^[a-z0-9_]+$")) {
            throw new IllegalArgumentException(
                    "Username can only contain lowercase letters, numbers and underscores.");
        }

        if (username.startsWith("_") || username.endsWith("_")) {
            throw new IllegalArgumentException("Username cannot start or end with an underscore.");
        }

        if (username.contains("__")) {
            throw new IllegalArgumentException("Username cannot contain consecutive underscores.");
        }
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new UsernameRegistration();
    }

    @Override
    protected UsernameRegistrationBuilder instance() {
        return this;
    }
}
