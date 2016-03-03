FROM java
VOLUME /tmp/generator/jsonschema
ADD target/vorto-jsonschema.jar vorto-jsonschema.jar
RUN bash -c 'touch vorto-jsonschema.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-jsonschema.jar --spring.profiles.active=eclipse"
