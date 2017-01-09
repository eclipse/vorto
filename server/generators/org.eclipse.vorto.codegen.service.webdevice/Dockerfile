FROM java:8
VOLUME /tmp/generator/webdevice
ADD target/vorto-webdevice.jar .
RUN bash -c 'touch vorto-webdevice.jar'
ENTRYPOINT /bin/bash -c "sleep 220; java -jar vorto-webdevice.jar"
