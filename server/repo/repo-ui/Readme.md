# Vorto Repository

The Vorto repository manages your device descriptions (Information Models). Users are able to look up device characteristics and services as well as convert the information models into executable, platform - specific code with the help of Code Generators. 

If you would like to find out more about available code generators, to set them up and use them directly from your repository, visit our [Vorto Code Generator Overview](../../generators/Readme.md).

## Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

  Open your browser and open the URL:

	 http://localhost:8080/infomodelrepository

The **local profile** is a profile for local **testing only**. It sets up the following accounts for you:

Test user Account:

	username: testuser
	password: testuser
 
Admin Account:

	username: admin
	password: admin

