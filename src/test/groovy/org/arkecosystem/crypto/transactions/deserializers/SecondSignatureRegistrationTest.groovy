package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.FixtureLoader
import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*

import java.util.List
import com.google.gson.Gson

class SecondSignatureRegistrationTest extends Specification {
    def "second-passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/second_signature_registration/second-passphrase")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.timestamp == fixture.data.timestamp
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.asset.signature == fixture.data.asset.signature
            actual.id == fixture.data.id
    }
}
