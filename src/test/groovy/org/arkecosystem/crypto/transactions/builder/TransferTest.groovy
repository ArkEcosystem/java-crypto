import spock.lang.Specification
import org.arkecosystem.crypto.transactions.builder.*

class TransferTest extends Specification {
    def "create with a passphrase"() {
        when:
            def actual = new Transfer()
                .recipient('AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25')
                .amount(133380000000)
                .vendorField('This is a transaction from PHP')
                .sign('this is a top secret passphrase')
        then:
            actual.getTransaction().verify()
    }

    def "create with a second passphrase"() {
        when:
            def actual = new Transfer()
                .recipient('AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25')
                .amount(133380000000)
                .vendorField('This is a transaction from PHP')
                .sign('this is a top secret passphrase')
                .secondSign('this is a top secret second passphrase')
        then:
            actual.getTransaction().verify()
    }
}
