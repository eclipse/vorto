# AWS Code Generator

This generator creates Node.js source code from an information model that can directly be deployed on AWS.

The Generator outputs:

 - AWS Lambda function that **reads** the status of a device from Thing Shadow Service
 - AWS Lambda function that **updates** the device's state in the Thing Shadow Service
 - AWS Alexa skill that allows users to read or update the device's state via a Voice Command

A Vorto Mapping can (optionally) be used to enhance the Alexa voice recognition. 


# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).