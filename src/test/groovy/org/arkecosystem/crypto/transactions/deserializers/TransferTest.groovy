package org.arkecosystem.crypto.transactions.deserializers

import spock.lang.Specification
import org.arkecosystem.crypto.transactions.*

class TransferTest extends Specification {
    def "deserialize with a passphrase"() {
        when:
            def actual = new Deserializer().deserialize('ff011e0019627802034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed19280969800000000000b48656c6c6f20576f726c6400c2eb0b00000000000000001e0995750207ecaf0ccf251c1265b92ad84f5536623045022100c0356f2f667c0d8df8370d9b6803a282cb142b8e85f6b87dfffe45e7aeeba8dc0220446b11b44e8a968a9d29a065e3dd1383a2a4c03efe663a7d1b9cf232df68c6d03045022100a16eaf2d869fe8ee57f5b611a5f7b6c9359e52797e99240ec2a56cc92d3df7a10220688c5ef2225a5b54adf92c43ea551963c8b1240d7a9fc93e670d5fa819b8f09f')
        then:
            actual.type == 0
            actual.amount == 200000000
            actual.fee == 10000000
            actual.recipientId == "D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib"
            actual.timestamp == 41443865
            actual.vendorField == "Hello World"
            actual.senderPublicKey == "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"
            actual.signature == "3045022100c0356f2f667c0d8df8370d9b6803a282cb142b8e85f6b87dfffe45e7aeeba8dc0220446b11b44e8a968a9d29a065e3dd1383a2a4c03efe663a7d1b9cf232df68c6d0"
            actual.signSignature == "3045022100a16eaf2d869fe8ee57f5b611a5f7b6c9359e52797e99240ec2a56cc92d3df7a10220688c5ef2225a5b54adf92c43ea551963c8b1240d7a9fc93e670d5fa819b8f09f"
            actual.id == "248e0c6159113659103a84dfa3882d82abefdbc0a30472b683c689d482cfb2f5"
    }
}
