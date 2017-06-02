
# Getting Started

 - Manage your Information Models with the [Vorto Repository](repo/repo-ui/Readme.md)
 - Convert Information Models into Platform Code with [Vorto Code Generators](generators/Readme.md)

# How to Docker Vorto Repository for the first time

Now Vorto can be spun as Docker containers. Follow the step to realize Vorto as containers.
- Setup MySql server as container and configure the root user and database for Vorto
- Define the MySql URL string in Vorto repo
- Start Vorto Repo as container
- Start Vorto Generator as container


## Setup the MySql Server Container

Run the MySql container with **root** password  and with *image* **Mysql** and  *tag* **5.7** with the following command
```
$ docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=passwordtext -d mysql:5.7
```

Now login as root to this MySql and create vorto user and vorto database, then grant all access to vorto user for all tables in vorto database.
```sql
mysql> create database vorto;
mysql> create user 'vorto'@'%' identified by 'passwordtext';
mysql> grant all privileges on vorto.* to 'vorto'@'%' with grant option;
```

## Update the MySql Database relevant properties in Vorto Repo
Ensure that the property **spring.datasource.url** redefined in the with **ip** of the *mysql* container.
```
spring.datasource.url=jdbc:mysql://<ip of mysql container>:3306/vorto
spring.datasource.username=vorto
spring.datasource.password=<passwordtext>
```

Save the above three properties to a file **env-repo.list**, which will be referred when launching the Vorto-repo as container.

## Start Vorto-Repo as a Container

Include two more properties to **env-repo.list** inorder to make use of a proper spring profile.
```
spring.profiles.active=eclipse
spring.config.location=config
```

Create an updated image from *eclipsevorto/vorto-repo* base image that includes mysql java connector jar and other default properties. Refer to [how to create an updated vorto image?](./howtocreatenewvortoimage.md)

Use the newly updated image of Vorto-repo to spin as a Docker container using the following command.

```
$ docker run -d -p8080:8080 --env-file ./env-repo.list --link mysql-container:mysql --name vorto-repo <updated_image_name>
```

## Start Vorto Generators as Containers

Here we have many generators that interface with Vorto-repo. We can either launch the generators as it is from the Docker Hub or after customizing the generator image by creating a new image.

### Spinning AWS Generator as Container from Docker Hub
There are a few properties that need to be set in **env-aws.list** that will be used in the following *docker run* command

**env-aws.list** contents:
```
vorto.service.repositoryUrl=http://<vorto-repo_container_ip>:8080/rest
server.contextPath=/vorto-aws
server.host=<ip_of_aws_generator_container>
server.port=9015
```

The above file is referred in the following command to spin AWS generator as container.
```
$ docker run -d -p 9015:9015 --env-file ./env-aws.list --name vorto-aws eclipsevorto/vorto-aws
```
