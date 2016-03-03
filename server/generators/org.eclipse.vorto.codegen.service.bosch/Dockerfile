FROM java
VOLUME /tmp/generator/bosch
ADD target/vorto-bosch.jar vorto-bosch.jar
RUN bash -c 'touch vorto-bosch.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-bosch.jar --spring.profiles.active=eclipse"
