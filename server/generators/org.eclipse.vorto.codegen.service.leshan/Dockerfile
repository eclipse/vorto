FROM java:8
VOLUME /tmp/generator/leshan
ADD target/vorto-leshan.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar vorto-leshan.jar --spring.profiles.active=docker"
