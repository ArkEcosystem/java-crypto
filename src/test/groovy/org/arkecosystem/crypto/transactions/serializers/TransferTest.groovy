package org.arkecosystem.crypto.transactions.serializers

import org.arkecosystem.crypto.FixtureLoader
import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*
import static com.google.common.io.BaseEncoding.base16

class TransferTest extends Specification {
    def "passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/passphrase")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            base16().lowerCase().encode(actual) == fixture.serialized
    }

    def "passphrase-with-vendor-field"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/passphrase-with-vendor-field")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            base16().lowerCase().encode(actual) == fixture.serialized
    }

    def "passphrase-with-vendor-field-hex"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/passphrase-with-vendor-field-hex")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            base16().lowerCase().encode(actual) == fixture.serialized
    }

    def "second-passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/second-passphrase")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            base16().lowerCase().encode(actual) == fixture.serialized
    }

    def "second-passphrase-with-vendor-field"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/second-passphrase-with-vendor-field")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            base16().lowerCase().encode(actual) == fixture.serialized
    }

    def "second-passphrase-with-vendor-field-hex"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/transfer/second-passphrase-with-vendor-field-hex")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            base16().lowerCase().encode(actual) == fixture.serialized
    }
}
