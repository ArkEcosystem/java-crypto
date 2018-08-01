import spock.lang.Specification
import org.arkecosystem.crypto.configuration.*
import org.arkecosystem.crypto.identities.*
import org.arkecosystem.crypto.networks.*

class AddressTest extends Specification {
    def "fromPassphrase"() {
        when:
            def actual = Address.fromPassphrase("this is a top secret passphrase")
        then:
            actual == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }

    def "fromPublicKey"() {
        when:
            def actual = Address.fromPublicKey("034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192")
        then:
            actual == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }

    def "fromPrivateKey"() {
        when:
            def privateKey = PrivateKey.fromPassphrase("this is a top secret passphrase")
            def actual = Address.fromPrivateKey(privateKey)
        then:
            actual == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }

    def "validate"() {
        when:
            Network.set(new Devnet())

            def actual = Address.validate("D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib")
        then:
            actual
    }
}
