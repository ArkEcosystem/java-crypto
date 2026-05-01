package org.arkecosystem.crypto.transactions.types;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.enums.AbiFunction;
import org.arkecosystem.crypto.enums.ContractAbiType;
import org.arkecosystem.crypto.utils.AbiDecoder;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class Multipayment extends AbstractTransaction {

    public Multipayment() {
        super();
        this.multipaymentRecipients = new ArrayList<>();
        this.multipaymentAmounts = new ArrayList<>();
    }

    public Multipayment(Map<String, Object> data) {
        super(data);
        this.multipaymentRecipients = new ArrayList<>();
        this.multipaymentAmounts = new ArrayList<>();

        List<Object> payload = decodeMultipaymentPayload(data);
        if (payload != null && payload.size() == 2) {
            List<String> recipients = (List<String>) payload.get(0);
            List<String> amounts = (List<String>) payload.get(1);
            this.multipaymentRecipients.addAll(recipients);
            for (String amount : amounts) {
                this.multipaymentAmounts.add(new BigInteger(amount));
            }
        }
    }

    @Override
    public String getPayload() {
        if (this.multipaymentRecipients == null || this.multipaymentRecipients.isEmpty()) {
            return "";
        }

        List<Object> args = Arrays.asList(this.multipaymentRecipients, this.multipaymentAmounts);

        try {
            return new AbiEncoder(ContractAbiType.MULTIPAYMENT)
                    .encodeFunctionCall(AbiFunction.MULTIPAYMENT.toString(), args);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding multipayment call", e);
        }
    }

    private static List<Object> decodeMultipaymentPayload(Map<String, Object> data) {
        if (data == null || !data.containsKey("data")) return null;

        String payload = (String) data.get("data");
        if (payload == null || payload.isEmpty()) return null;

        try {
            AbiDecoder decoder = new AbiDecoder(ContractAbiType.MULTIPAYMENT);
            Map<String, Object> decoded = decoder.decodeFunctionData(payload);
            return (List<Object>) decoded.get("args");
        } catch (Exception e) {
            return null;
        }
    }
}
