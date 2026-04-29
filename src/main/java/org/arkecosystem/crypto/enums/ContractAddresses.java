package org.arkecosystem.crypto.enums;

public enum ContractAddresses {
    CONSENSUS("0x535B3D7A252fa034Ed71F0C53ec0C6F784cB64E1"),
    MULTIPAYMENT("0x00EFd0D4639191C49908A7BddbB9A11A994A8527"),
    USERNAMES("0x2c1DE3b4Dbb4aDebEbB5dcECAe825bE2a9fc6eb6");

    private final String address;

    ContractAddresses(String address) {
        this.address = address;
    }

    public String address() {
        return address;
    }
}
