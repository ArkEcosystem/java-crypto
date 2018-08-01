import spock.lang.Specification
import org.arkecosystem.crypto.identities.*

class PublicKeyTest extends Specification {
    def "fromPassphrase"() {
        when:
            def actual = PublicKey.fromPassphrase("this is a top secret passphrase")
        then:
            actual == '034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'
    }
}
