import spock.lang.Specification
import org.arkecosystem.crypto.transactions.builder.*

class SecondSignatureRegistrationTest extends Specification {
    def "create with a second passphrase"() {
        when:
            def actual = new SecondSignatureRegistration()
                .signature('this is a top secret second passphrase')
                .sign('this is a top secret passphrase')
        then:
            actual.getTransaction().verify()
    }
}
