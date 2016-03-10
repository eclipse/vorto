FROM java:8
VOLUME /tmp
COPY repo-ui/target/infomodelrepository.jar .
ENTRYPOINT ["java","-jar","infomodelrepository.jar","--spring.profiles.active=eclipse"]
