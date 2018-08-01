import spock.lang.Specification
import org.arkecosystem.crypto.configuration.*
import org.arkecosystem.crypto.networks.*

class NetworkTest extends Specification {
    def "get"() {
        when:
            def network = Network.get()
        then:
            network.version() == new Devnet().version()
    }

    def "set"() {
        when:
            Network.set(new Testnet())
            def network = Network.get()
        then:
            network.version() == new Testnet().version()
            Network.set(new Devnet())
    }
}
