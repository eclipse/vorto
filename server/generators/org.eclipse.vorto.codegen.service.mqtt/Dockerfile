FROM java
VOLUME /tmp/generator/mqtt
ADD target/vorto-mqtt.jar vorto-mqtt.jar
RUN bash -c 'touch vorto-mqtt.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-mqtt.jar --spring.profiles.active=eclipse"
