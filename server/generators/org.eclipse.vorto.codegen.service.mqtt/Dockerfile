FROM java:8
VOLUME /tmp/generator/mqtt
ADD target/vorto-mqtt.jar .
ENTRYPOINT /bin/bash -c "sleep 180; java -jar vorto-mqtt.jar"
