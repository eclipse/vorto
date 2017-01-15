FROM java:8
VOLUME /tmp/generator/boschthings
ADD target/vorto-bosch-iot-things.jar .
ENTRYPOINT /bin/bash -c "sleep 280; java -jar vorto-bosch-iot-things.jar"
