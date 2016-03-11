FROM java:8
VOLUME /tmp/generator/thingworx
ADD target/vorto-thingworx.jar .
ENTRYPOINT /bin/bash -c "sleep 200; java -jar vorto-thingworx.jar --spring.profiles.active=docker"
