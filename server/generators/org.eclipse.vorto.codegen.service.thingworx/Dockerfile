FROM java:8
VOLUME /tmp/generator/thingworx
ADD target/vorto-thingworx.jar vorto-thingworx.jar
RUN bash -c 'touch vorto-thingworx.jar'
ENTRYPOINT ["java","-jar","vorto-thingworx.jar"]