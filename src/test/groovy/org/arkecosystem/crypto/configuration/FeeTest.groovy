package org.arkecosystem.crypto.configuration

import spock.lang.Specification
import org.arkecosystem.crypto.configuration.*
import org.arkecosystem.crypto.enums.*

class FeeTest extends Specification {
    def "get"() {
        when:
            def fee = Fee.get(Types.TRANSFER.getValue())
        then:
            fee == 10000000
    }

    def "set"() {
        when:
            Fee.set(Types.TRANSFER.getValue(), 50000000)
            def fee = Fee.get(Types.TRANSFER.getValue())
        then:
            fee == 50000000
    }
}
