# How to create a new Vorto-Repo image?
* Pull the image *eclipsevorto/vorto-repo* from *Docker Hub*
* Create a project folder example: **vorto-repo-docker** and *cd* to that folder 
* Copy all the necessary files that we need in order to create this image to support access to MySql server and to use eclipse profile and other resources. Following resources are required to be in this folder.
    * **mysql-connector-java-5.1.37.jar** to the sub-folder '*lib*' within vorto-repo-docker folder
    * create **application-eclipse.yml** file in the sub-folder '*config*' within vorto-repo-docker folder. YML file contains all the default _key:value_ pairs need to start vorto-repo

**application-eclipse.yml** contents:
```
    server:
      port: 8080
      contextPath:
     
    spring:
      profiles: eclipse
      datasource:
        testWhileIdle: true
        timeBetweenEvictionRunsMillis: 60000
        validationQuery: SELECT 1
        driverClassName: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/vorto
     
      jpa:
        properties:
          hibernate:
            show_sql: true
            format_sql: true
        show-sql: true
        generate-ddl: true
      hibernate:
        ddl-auto: validate
     
    logging:
      level:
        org:
          hibernate:
            type: info
        org.springframework.security: INFO
     
    mail:
      smtp:
        host: localhost
        port: 25
      from: vorto-dev@eclipse.org
```

* Create **Dockerfile** that is used to build a new version of Vorto-Repo image that contains mysql

**Dockerfile** contents:
> FROM eclipsevorto/vorto-repo
> ADD lib/mysql-connector-java-5.1.37.jar /lib/
> RUN mkdir config
> ENV CLASSPATH=.:lib/*:infomodelrepository.jar
> ADD config/application-eclipse.yml config/
> ENTRYPOINT ["java","-cp","${CLASSPATH}:lib/*:infomodelrepository.jar","org.springframework.boot.loader.JarLauncher"]

* Build the new image of *vorto-repo:version2* from *eclipsevorto/vorto-repo* using the above *Dockerfile* in the vorto-repo-docker folder.
```
$ docker build -t vorto-repo:version2 .
```

