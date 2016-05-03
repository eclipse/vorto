FROM java:8
VOLUME /tmp/generator/bosch
ADD target/vorto-bosch.jar .
ENTRYPOINT /bin/bash -c "sleep 120; java -jar vorto-bosch.jar --spring.profiles.active=docker"
