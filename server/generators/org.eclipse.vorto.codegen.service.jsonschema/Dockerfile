FROM java:8
VOLUME /tmp/generator/jsonschema
ADD target/vorto-jsonschema.jar .
ENTRYPOINT /bin/bash -c "sleep 280; java -jar vorto-jsonschema.jar --spring.profiles.active=docker"
