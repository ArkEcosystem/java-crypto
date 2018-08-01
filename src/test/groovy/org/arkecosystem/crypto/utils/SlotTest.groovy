import spock.lang.Specification
import org.arkecosystem.crypto.utils.*

class SlotTest extends Specification {
    def "time"() {
        when:
            def actual = Slot.time()
        then:
            actual instanceof Integer
    }

    def "epoch"() {
        when:
            def actual = Slot.epoch()
        then:
            actual.toString() == 'Tue Mar 21 15:00:00 EET 2017'
    }
}
