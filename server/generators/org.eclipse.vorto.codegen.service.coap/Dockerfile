FROM java:8
VOLUME /tmp/generator/coap
ADD target/vorto-coap.jar .
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-coap.jar --spring.profiles.active=docker"
