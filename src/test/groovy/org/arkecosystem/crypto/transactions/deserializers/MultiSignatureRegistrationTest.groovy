package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.FixtureLoader
import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*

import java.util.List
import com.google.gson.Gson

class MultiSignatureRegistrationTest extends Specification {
    def "passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/multi_signature_registration/passphrase")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            // actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signatures == fixture.data.signatures
            actual.asset.multisignature.min == fixture.data.asset.multisignature.min
            actual.asset.multisignature.keysgroup == fixture.data.asset.multisignature.keysgroup
            actual.asset.multisignature.lifetime == fixture.data.asset.multisignature.lifetime
            actual.id == fixture.data.id
    }
}
