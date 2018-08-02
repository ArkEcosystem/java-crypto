package org.arkecosystem.crypto.transactions.serializers

import org.arkecosystem.crypto.FixtureLoader
import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*
import org.arkecosystem.crypto.encoding.*

class VoteTest extends Specification {
    def "passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/vote/passphrase")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            Hex.encode(actual) == fixture.serialized
    }

    def "second-passphrase"() {
        setup:
            Object fixture = FixtureLoader.load(getClass(), "transactions/vote/second-passphrase")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            Hex.encode(actual) == fixture.serialized
    }
}
