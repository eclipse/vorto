# Vorto Repository

The Vorto repository manages your device descriptions (Information Models). Users are able to look up device characteristics and services as well as convert the information models into executable, platform - specific code with the help of Code Generators. 

If you would like to find out more about available code generators, to set them up and use them directly from your repository, visit our [Vorto Code Generator Overview](../../generators/Readme.md).

## GitHub OAuth App
- Open https://github.com/settings/developers
- Under Settings->Developer Settings->OAuth Apps click `Register a new application`
- Fill in the `Application name` with what ever you want, we are using `Vorto Local`
- Fill in the `Homepage URL` with `https://localhost:8443/infomodelrepository/`
- Fill in the `Authorization callback URL` with `https://localhost:8443/infomodelrepository`
- Copy the shown `Client ID` and `Client Secret` for use in the next section

## Build & Run

#### In-memory H2 database (Default):

By default, the Vorto repository is configured to run with in-memory hsql database. Open your command line client and navigate to 
> /vorto/repository/repository-server

To start the Vorto Repository locally run command

    mvn spring-boot:run -Dspring.profiles.active=local-https -Dgithub_clientid=<YOUR_CLIENT_ID> -Dgithub_clientSecret=<YOUR_CLIENT_SECRET> -Deidp_clientid=ciamids_12345 -Deidp_clientSecret=12345

If you are behind a corporate proxy add additional proxy parameters 

    -Dhttps.proxyHost=<PROXY_HOST_NAME> -Dhttps.proxyPort=<PROXY_PORT> -Dhttps.proxyUser=<PROXY_USER> -Dhttps.proxyPassword=<PROXY_PASSWORD>


Once you see the message `Started VortoRepository in xx.xxx seconds`. Open your browser and navigate to:

> https://localhost:8443/infomodelrepository 

#### MySQL database:

If you would like to persist your models locally between restarts use MySQL as your data store. Follow the steps below to get started:

1. Install MySQL Server Community Edition [5.6.45](https://dev.mysql.com/downloads/windows/installer/5.6.html "mysql 5.6.45") or use a Docker [image](https://hub.docker.com/r/mysql/mysql-server/tags?page=1&name=5.6 "docker image")
2. Open MySQL command line and create your user and schema

        CREATE DATABASE localvortodb CHARACTER SET utf8 COLLATE utf8_general_ci;
        CREATE USER 'vorto_user'@'localhost' IDENTIFIED BY 'password';
        GRANT ALL PRIVILEGES ON localvortodb.* to 'vorto_user'@'localhost';
        FLUSH PRIVILEGES;

3. Make changes to three files for using your database for persistence:
   1. **application-local-https.yml**: add configuration

            repo:
              configFile: vorto-repository-config-mysql.json

            spring:
              datasource:
                  testWhileIdle: true
                  timeBetweenEvictionRunsMillis: 60000
                  wait_timeout: 180
                  validationInterval: 60    
                  validationQuery: SELECT 1
                  driver-class-name: org.mariadb.jdbc.Driver
                  url: jdbc:mysql://localhost:3306/localvortodb?autoReconnect=true
                  username: vorto_user
                  password: password
                jpa:
                  generate-ddl: true
                hibernate:
                  ddl-auto: validate

   2. **vorto-repository-config-mysql.json**: update binaryStorage url, username and password fields
   3. **infinispan-configuration-mysql.xml**: update connection-url, username and password fields

----------
Back to [Vorto Server Overview](../../Readme.md)

