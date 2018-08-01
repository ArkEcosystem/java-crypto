import spock.lang.Specification
import org.arkecosystem.crypto.identities.*

class WIFTest extends Specification {
    def "fromPassphrase"() {
        when:
            def actual = WIF.fromPassphrase("this is a top secret passphrase")
        then:
            actual == 'SGq4xLgZKCGxs7bjmwnBrWcT4C1ADFEermj846KC97FSv1WFD1dA'
    }
}
