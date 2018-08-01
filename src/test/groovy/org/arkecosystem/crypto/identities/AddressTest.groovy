import spock.lang.Specification
import org.arkecosystem.crypto.identities.*

class AddressTest extends Specification {
    def "fromPassphrase"() {
        when:
            def address = Address.fromPassphrase("this is a top secret passphrase")
        then:
            address == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }

    def "fromPublicKey"() {
        when:
            def address = Address.fromPublicKey("034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192")
        then:
            address == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }

    def "fromPrivateKey"() {
        when:
            def privateKey = PrivateKey.fromPassphrase("this is a top secret passphrase")
            def address = Address.fromPrivateKey(privateKey)
        then:
            address == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }
}
