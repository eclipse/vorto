FROM java:8
VOLUME /tmp/generator/lwm2m
ADD target/vorto-lwm2m.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar vorto-lwm2m.jar --spring.profiles.active=docker"
