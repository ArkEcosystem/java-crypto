# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Unreleased

## 0.1.6 - 2019-10-15

### Changed
- Downgrade bitcoinj-core to 0.14.7

## 0.1.5 - 2019-10-07

### Changed
- Updated dependencies

## 0.1.4 - 2019-11-06

### Fixed
- Support 255 bytes vendor fields

## 0.1.3 - 2019-25-04

### Fixed
- Adjusted the JSON to Java Object conversion to support stricter incoming validation on the core.

## 0.1.2 - 2018-12-11

### Changed
- Set `network` field on transaction

## 0.1.1 - 2018-12-11

### Fixed

- Construct the correct JSON representation of transactions
- Return the correct transaction type for `MultiSignatureRegistration`
- Properly cast slot times to integer
- Set the version on `AbstractTransaction`

## 0.1.0 - 2018-10-08

- Initial Release
