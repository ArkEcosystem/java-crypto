package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.FixtureLoader
import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*

import java.util.List
import com.google.gson.Gson

class TransferTest
extends Specification {
    def "passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/passphrase")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.vendorField == fixture.data.vendorField
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.id == fixture.data.id
    }

    def "passphrase-with-vendor-field"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/passphrase-with-vendor-field")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.vendorField == fixture.data.vendorField
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.id == fixture.data.id
    }

    def "passphrase-with-vendor-field-hex"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/passphrase-with-vendor-field-hex")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.vendorFieldHex == fixture.data.vendorFieldHex
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.id == fixture.data.id
    }

    def "second-passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/second-passphrase")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.vendorField == fixture.data.vendorField
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.id == fixture.data.id
    }

    def "second-passphrase-with-vendor-field"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/second-passphrase-with-vendor-field")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.vendorField == fixture.data.vendorField
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.id == fixture.data.id
    }

    def "second-passphrase-with-vendor-field-hex"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/second-passphrase-with-vendor-field-hex")
        when:
            def actual = new Deserializer().deserialize(fixture.serialized)
        then:
            actual.type == fixture.data.type
            actual.amount == fixture.data.amount
            actual.fee == fixture.data.fee
            actual.recipientId == fixture.data.recipientId
            actual.timestamp == fixture.data.timestamp
            actual.vendorFieldHex == fixture.data.vendorFieldHex
            actual.senderPublicKey == fixture.data.senderPublicKey
            actual.signature == fixture.data.signature
            actual.signSignature == fixture.data.signSignature
            actual.id == fixture.data.id
    }
}
