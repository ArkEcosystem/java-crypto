package org.arkecosystem.crypto.transactions.serializers

import org.arkecosystem.crypto.FixtureLoader
import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*
import org.arkecosystem.crypto.configuration.Network
import org.arkecosystem.crypto.networks.*
import org.arkecosystem.crypto.encoding.*

class MultiSignatureRegistrationTest extends Specification {
    def "passphrase"() {
        setup:
            Network.set(new Mainnet())
            Object fixture = FixtureLoader.load(getClass(), "transactions/multi_signature_registration/passphrase")
        when:
            def actual = new Serializer().serialize(FixtureLoader.transaction(fixture))
        then:
            Hex.encode(actual) == fixture.serialized
            Network.set(new Devnet())
    }
}
