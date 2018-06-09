FROM java:8
VOLUME /tmp/generator/gateway
ADD target/vorto-gateway.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar vorto-gateway.jar"