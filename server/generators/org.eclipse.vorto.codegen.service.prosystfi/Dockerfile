FROM java:8
VOLUME /tmp/generator/prosyst
ADD target/vorto-prosyst.jar .
ENTRYPOINT /bin/bash -c "sleep 220; java -jar vorto-prosyst.jar"
