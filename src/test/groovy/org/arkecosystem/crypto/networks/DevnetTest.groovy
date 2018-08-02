package org.arkecosystem.crypto.networks

import spock.lang.Specification
import org.arkecosystem.crypto.networks.*

class DevnetTest extends Specification {
    def "version"() {
        when:
            def actual = new Devnet().version()
        then:
            actual == 0x1e.byteValue()
    }

    def "wif"() {
        when:
            def actual = new Devnet().wif()
        then:
            actual == 170
    }

    def "epoch"() {
        when:
            def actual = new Devnet().epoch()
        then:
            actual =='2017-03-21 13:00:00'
    }
}
