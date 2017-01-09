FROM java:8
VOLUME /tmp/generator/ios
ADD target/vorto-ios.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar vorto-ios.jar"
