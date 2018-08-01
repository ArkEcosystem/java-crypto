import spock.lang.Specification
import org.arkecosystem.crypto.networks.*

class MainnetTest extends Specification {
    def "addressByte"() {
        when:
            def actual = new Mainnet().addressByte()
        then:
            actual == 0x17.byteValue()
    }

    def "wif"() {
        when:
            def actual = new Mainnet().wif()
        then:
            actual == 0xaa.byteValue()
    }

    def "epoch"() {
        when:
            def actual = new Mainnet().epoch()
        then:
            actual =='2017-03-21 13:00:00'
    }
}
