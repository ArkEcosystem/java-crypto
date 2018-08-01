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
            actual.getTime() == 1490101200000
    }
}
