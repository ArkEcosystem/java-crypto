package org.arkecosystem.crypto

import com.google.gson.Gson
import org.arkecosystem.crypto.transactions.Transaction

class FixtureLoader {
    static File raw(instance, String path) {
        ClassLoader classLoader = instance.getClassLoader()

        URL resource = classLoader.getResource(String.format("%s.json", path))

        return new File(resource.getPath())
    }

    static Object load(instance, String path) {
        return new Gson().fromJson(raw(instance, path).text, Object.class)
    }

    static Transaction transaction(Object fixture) {
        def transaction = new Transaction()
        transaction.type = fixture.data.type
        transaction.amount = fixture.data.amount
        transaction.fee = fixture.data.fee
        transaction.recipientId = fixture.data.recipientId
        transaction.timestamp = fixture.data.timestamp
        transaction.vendorField = fixture.data.vendorField
        transaction.vendorFieldHex = fixture.data.vendorFieldHex
        transaction.senderPublicKey = fixture.data.senderPublicKey
        transaction.signature = fixture.data.signature
        transaction.signSignature = fixture.data.signSignature
        transaction.signatures = fixture.data.signatures
        transaction.asset = fixture.data.asset
        transaction.id = fixture.data.id

        return transaction
    }
}
