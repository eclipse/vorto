FROM java:8
VOLUME /tmp/generator/bosch
ADD target/vorto-bosch.jar vorto-bosch.jar
RUN bash -c 'touch vorto-bosch.jar'
ENTRYPOINT ["java","-jar","vorto-bosch.jar"]