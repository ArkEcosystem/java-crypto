# ARK Java - Crypto

<p align="center">
    <img src="https://github.com/ArkEcosystem/java-crypto/blob/master/banner.png" />
</p>

> A simple Cryptography Implementation in Java for the ARK Blockchain.

[![Build Status](https://img.shields.io/travis/ArkEcosystem/java-crypto/master.svg)](https://travis-ci.org/ArkEcosystem/java-crypto)
[![Codecov](https://img.shields.io/codecov/c/github/arkecosystem/java-crypto.svg)](https://codecov.io/gh/arkecosystem/java-crypto)
[![Latest Version](https://img.shields.io/github/release/ArkEcosystem/java-crypto.svg)](https://github.com/ArkEcosystem/java-crypto/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## TO-DO

### AIP11 Serialization
- [ ] Transfer
- [ ] Second Signature Registration
- [ ] Delegate Registration
- [ ] Vote
- [ ] Multi Signature Registration
- [ ] IPFS
- [ ] Timelock Transfer
- [ ] Multi Payment
- [ ] Delegate Resignation

### AIP11 Deserialization
- [ ] Transfer
- [ ] Second Signature Registration
- [ ] Delegate Registration
- [ ] Vote
- [ ] Multi Signature Registration
- [ ] IPFS
- [ ] Timelock Transfer
- [ ] Multi Payment
- [ ] Delegate Resignation

### Transaction Signing
- [ ] Transfer
- [ ] Second Signature Registration
- [ ] Delegate Registration
- [ ] Vote
- [ ] Multi Signature Registration

### Transaction Verifying
- [ ] Transfer
- [ ] Second Signature Registration
- [ ] Delegate Registration
- [ ] Vote
- [ ] Multi Signature Registration

### Transaction
- [x] getId
- [x] sign
- [x] secondSign
- [x] verify
- [x] secondverify
- [ ] parseSignatures
- [ ] serialize
- [ ] deserialize
- [x] toBytes
- [x] toArray
- [x] toJson

### Message
- [x] sign
- [x] verify
- [x] toArray
- [x] toJson

### Address
- [x] fromPassphrase
- [x] fromPublicKey
- [x] fromPrivateKey
- [x] validate

### Private Key
- [x] fromPassphrase
- [x] fromHex

### Public Key
- [x] fromPassphrase
- [ ] ~~fromHex~~

### WIF
- [ ] fromPassphrase

### Configuration
- [x] getNetwork
- [x] setNetwork
- [x] getFee
- [x] setFee

### Slot
- [x] time
- [x] epoch

### Networks (Mainnet, Devnet & Testnet)
- [x] epoch
- [x] version
- [x] wif

## Documentation

You can find installation instructions and detailed instructions on how to use this package at the [dedicated documentation site](https://docs.ark.io/v1.0/docs/cryptography-java).

## Security

If you discover a security vulnerability within this package, please send an e-mail to security@ark.io. All security vulnerabilities will be promptly addressed.

## Credits

- [Brian Faust](https://github.com/faustbrian)
- [All Contributors](../../../../contributors)

## License

[MIT](LICENSE) © [ArkEcosystem](https://ark.io)
