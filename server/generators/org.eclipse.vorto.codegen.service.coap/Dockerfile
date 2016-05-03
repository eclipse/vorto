FROM java:8
VOLUME /tmp/generator/coap
ADD target/vorto-coap.jar .
ENTRYPOINT /bin/bash -c "sleep 260; java -jar vorto-coap.jar --spring.profiles.active=docker"
