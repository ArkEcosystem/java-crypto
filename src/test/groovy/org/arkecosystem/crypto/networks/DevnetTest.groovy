import spock.lang.Specification
import org.arkecosystem.crypto.networks.*

class DevnetTest extends Specification {
    def "addressByte"() {
        when:
            def actual = new Devnet().addressByte()
        then:
            actual == 0x1e.byteValue()
    }

    def "wif"() {
        when:
            def actual = new Devnet().wif()
        then:
            actual == 0xaa.byteValue()
    }

    def "epoch"() {
        when:
            def actual = new Devnet().epoch()
        then:
            actual == '2017-03-21T13:00:00.000Z'
    }
}
