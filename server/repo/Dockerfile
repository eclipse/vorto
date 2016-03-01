FROM java:8
VOLUME /tmp
ADD infomodelrepository.jar infomodelrepository.jar
RUN bash -c 'touch infomodelrepository.jar'
ENTRYPOINT ["java","-jar","infomodelrepository.jar"]