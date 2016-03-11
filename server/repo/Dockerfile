FROM java:8
VOLUME /tmp
ADD repo-ui/target/infomodelrepository.jar .
ENTRYPOINT ["java","-jar","infomodelrepository.jar","--spring.profiles.active=eclipse"]
