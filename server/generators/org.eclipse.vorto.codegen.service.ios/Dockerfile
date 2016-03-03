FROM java
VOLUME /tmp/generator/ios
ADD target/vorto-ios.jar vorto-ios.jar
RUN bash -c 'touch vorto-ios.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-ios.jar --spring.profiles.active=eclipse"
