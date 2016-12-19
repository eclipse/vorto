FROM java:8
VOLUME /tmp/generator/boschiotthings
ADD target/vorto-boschiotthings.jar .
ENTRYPOINT /bin/bash -c "sleep 280; java -jar vorto-boschiotthings.jar --spring.profiles.active=docker"
