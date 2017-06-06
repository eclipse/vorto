FROM java:8
VOLUME /tmp/generator/webui
ADD target/vorto-webui.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar vorto-webui.jar"
