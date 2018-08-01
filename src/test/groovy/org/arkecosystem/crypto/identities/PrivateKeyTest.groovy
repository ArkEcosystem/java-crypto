import spock.lang.Specification
import org.arkecosystem.crypto.identities.*

class PrivateKeyTest extends Specification {
    def "fromPassphrase"() {
        when:
            def actual = PrivateKey.fromPassphrase("this is a top secret passphrase").getPrivateKeyAsHex()
        then:
            actual == 'd8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712'
    }

    def "fromHex"() {
        when:
            def actual = PrivateKey.fromHex("d8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712").getPrivateKeyAsHex()
        then:
            actual == 'd8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712'
    }
}
