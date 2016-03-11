FROM java:8
VOLUME /tmp/generator/ios
ADD target/vorto-ios.jar .
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-ios.jar --spring.profiles.active=docker"
