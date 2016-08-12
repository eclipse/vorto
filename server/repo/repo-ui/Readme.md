# Vorto Repository

The Vorto repository manages your device descriptions (Information Models). Users are able to look up device characteristics and services as well as convert the information models into executable, platform - specific code with the help of Code Generators. 

If you would like to find out more about available code generators, to set them up and use them directly from your repository, visit our [Vorto Code Generator Overview](../../generators/Readme.md).

## Build & Run

#### In memory hsql database (Default):

By default, the Vorto repository is configured to run with in memory hsql database when you run the below command.  

	mvn clean install spring-boot:run -Dspring.profiles.active=local


The **local profile** is a profile for local **testing only**. It sets up the following accounts for you:

Test user Account:

	username: testuser
	password: testuser
 

Open your browser and open the URL:

	 http://localhost:8080/infomodelrepository 
 
#### Mysql database: 
Alternatively, Vorto repository can be configured to run with other databases as well. 
For example to run Vorto repository with Mysql database, follow the below steps. 

Edit [pom.xml](../pom.xml), and add mysql connector maven dependency,

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.37</version>
		</dependency>

In command prompt, invoke 

	mvn clean install spring-boot:run -Dspring.profiles.active=mysql
  

> Note: Provide mysql username and password in [application-mysql.yml](src/main/resources/application-mysql.yml) against database username and password fields.

Open your browser and open the URL:

	 http://localhost:8080/infomodelrepository 



----------
----------
Back to [Vorto Server Overview](../../Readme.md)

