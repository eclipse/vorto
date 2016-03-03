FROM java
VOLUME /tmp/generator/thingworx
ADD target/vorto-thingworx.jar vorto-thingworx.jar
RUN bash -c 'touch vorto-thingworx.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-thingworx.jar --spring.profiles.active=eclipse"
