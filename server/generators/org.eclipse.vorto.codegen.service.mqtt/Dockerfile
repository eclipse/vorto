FROM java:8
VOLUME /tmp/generator/mqtt
ADD target/vorto-mqtt.jar vorto-mqtt.jar
RUN bash -c 'touch vorto-mqtt.jar'
ENTRYPOINT ["java","-jar","vorto-mqtt.jar"]