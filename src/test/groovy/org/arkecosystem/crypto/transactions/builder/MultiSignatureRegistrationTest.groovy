package org.arkecosystem.crypto.transactions.builder

import spock.lang.Specification
import org.arkecosystem.crypto.transactions.builder.*

class MultiSignatureRegistrationTest extends Specification {
    def "create with a passphrase"() {
        when:
            def actual = new MultiSignatureRegistration()
                .min(2)
                .lifetime(255)
                .keysgroup([
                    '03a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933',
                    '13a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933',
                    '23a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933',
                ])
                .sign('this is a top secret passphrase')
        then:
            actual.getTransaction().verify()
    }

    def "create with a second passphrase"() {
        when:
            def actual = new MultiSignatureRegistration()
                .min(2)
                .lifetime(255)
                .keysgroup([
                    '03a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933',
                    '13a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933',
                    '23a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933',
                ])
                .sign('this is a top secret passphrase')
                .secondSign('this is a top secret second passphrase')
        then:
            actual.getTransaction().verify()
    }
}
