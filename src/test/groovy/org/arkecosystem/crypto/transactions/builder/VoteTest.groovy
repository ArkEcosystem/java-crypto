package org.arkecosystem.crypto.transactions.builder

import spock.lang.Specification
import org.arkecosystem.crypto.transactions.builder.*

class VoteTest extends Specification {
    def "create with a passphrase"() {
        when:
            def actual = new Vote()
                .votes(['+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'])
                .sign('this is a top secret passphrase')
        then:
            actual.getTransaction().verify()
    }

    def "create with a second passphrase"() {
        when:
            def actual = new Vote()
                .votes(['+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'])
                .sign('this is a top secret passphrase')
                .secondSign('this is a top secret second passphrase')
        then:
            actual.getTransaction().verify()
    }
}
