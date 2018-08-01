import spock.lang.Specification
import org.arkecosystem.crypto.networks.*

class TestnetTest extends Specification {
    def "addressByte"() {
        when:
            def actual = new Testnet().addressByte()
        then:
            actual == 0x17.byteValue()
    }

    def "wif"() {
        when:
            def actual = new Testnet().wif()
        then:
            actual == 0xba.byteValue()
    }

    def "epoch"() {
        when:
            def actual = new Testnet().epoch()
        then:
            actual =='2017-03-21 13:00:00'
    }
}
