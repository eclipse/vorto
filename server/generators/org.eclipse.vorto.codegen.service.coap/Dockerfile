FROM java
VOLUME /tmp/generator/coap
ADD target/vorto-coap.jar vorto-coap.jar
RUN bash -c 'touch vorto-coap.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-coap.jar --spring.profiles.active=eclipse"
