FROM java:8
VOLUME /tmp/generator/jsonschema
ADD target/vorto-jsonschema.jar vorto-jsonschema.jar
RUN bash -c 'touch vorto-jsonschema.jar'
ENTRYPOINT ["java","-jar","vorto-jsonschema.jar"]