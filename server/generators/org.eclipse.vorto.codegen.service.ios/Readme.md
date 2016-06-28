# iOS Code Generator

This generator supports the iOS platform by translating an information model into Swift object model as well as adding support for BLE stack.

The Generator outputs:

 - iOS object model for an Information Model
 - BLE device registration- and Message Services


# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).