import spock.lang.Specification
import org.arkecosystem.crypto.transactions.builder.*

class DelegateRegistrationTest extends Specification {
    def "create with a passphrase"() {
        when:
            def actual = new DelegateRegistration()
                .username('polopolo')
                .sign('this is a top secret passphrase')
        then:
            actual.getTransaction().verify()
    }

    def "create with a second passphrase"() {
        when:
            def actual = new DelegateRegistration()
                .username('polopolo')
                .sign('this is a top secret passphrase')
                .secondSign('this is a top secret second passphrase')
        then:
            actual.getTransaction().verify()
    }
}
