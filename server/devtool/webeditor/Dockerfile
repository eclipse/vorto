FROM java:8
VOLUME /tmp/webeditor
ADD target/webeditor.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar webeditor.jar"
