# Vorto Repository

The Vorto repository manages your device descriptions (Information Models). Users are able to look up device characteristics and services as well as convert the information models into executable, platform - specific code with the help of Code Generators. 

If you would like to find out more about available code generators, to set them up and use them directly from your repository, visit our [Vorto Code Generator Overview](../../generators/Readme.md).

## Quickstart

Eager to try out the vorto model repository? This section gives a good introduction on howto set up your repository in a couple of minutes.
It assumes you already have `docker-compose` installed and created an GitHub OAuth App. If you do not, head over to the [docker installation](https://docs.docker.com/compose/install/) and after to the [GitHub OAuth App section]().

So the first thing you do, is cloning this repository by running: 
`git clone https://github.com/eclipse/vorto-examples.git vorto`

The next step is to replace the GitHub tokens
```json
"github_clientid":"your_client_id",
"github_secret":"your_github_secret"
```
in the `docker/config.json`
Followed by replacing the admin user with your GitHub Username. To allow multiple admin users pass a `;` sperated list.
```json
"server": {
   "admin": "your_github_user"
}
```

If you are running this setup behind a proxy check the [proxy section] on what to do.

To finally run the repository and the generators run `docker-compose up`

As soon everything is running you can reach your repository under `http://localhost:8080/infomodelrepository`.
To fill the repository with models see the [import section](#Importing).

## Docker

This repository can also be launched by using `docker-compose` or `docker`.
The current milestone images are provided on [dockerhub](https://hub.docker.com/u/eclipsevorto/)

### Configure

This Docker image exposes the port 8080 and expects a bind mount of the docker folder into `/code/config/` to read the configuration.
The configuration is provided using the `config.json` file, it contains the configuration for the repoisitory, [the generators](../repository-generators/Readme.md) and [the 3rd party generators](https://github.com/eclipse/vorto-examples).
If the enviroment variable is set to `USE_PROXY=1` then the proxy settings are read. The complete config file is passed to [Spring Boot](https://spring.io/projects/spring-boot) using the `SPRING_APPLICATION_JSON` env var.

### Build
This Docker image expects the build arg `JAR_FILE` to be passed, pointing to the jar file thats supposed to be run in the container.
If you are using the maven build pipline you can run `docker build -t repo --build-arg JAR_FILE=infomodelrepository.jar`. Don't forget to pass `--build-arg http_proxy` if you are running this build behind a proxy.

### Proxy
If you are running this setup behind a proxy you need to fill in proxy settings with the correct values and set `USE_PROXY` in the `docker-compose.yml` to `1`.
If you build this docker container behind a proxy you need to append the following build-args `http_proxy=http://user:password@proxy_url:8080` and `https_proxy=https://user:password@proxy_url:8080`.
Docker does not put those vars in the resulting image so proxt user and proxy password are not exposed.

### GitHub OAuth App
- Open https://github.com/settings/developers
- Under Settings->Developer Settings->OAuth Apps click `Register a new application`
- Fill in the `Application name` with what ever you want, we are using `Vorto Local`
- Fill in the `Homepage URL` with `http://localhost:8080/infomodelrepository/`
- Fill in the `Authorization callback URL` with `http://localhost:8080/infomodelrepository/github/login`
Copy the shown `Client ID` and `Client Secret` to your config.json

## Importing <a name="Importing"></a>

To import models into the repository you have to login with an administrator account. After that you can upload and xml under the Import section.

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

