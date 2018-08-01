import spock.lang.Specification
import org.arkecosystem.crypto.identities.*

class PrivateKeyTest extends Specification {
    def "fromPassphrase"() {
        when:
            def privateKey = PrivateKey.fromPassphrase("this is a top secret passphrase").getPrivateKeyAsHex()
        then:
            privateKey == 'd8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712'
    }
}
