FROM java
VOLUME /tmp/generator/webdevice
ADD target/vorto-webdevice.jar vorto-webdevice.jar
RUN bash -c 'touch vorto-webdevice.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-webdevice.jar --spring.profiles.active=eclipse"
