FROM java:8
VOLUME /tmp/generator/javabean
ADD target/vorto-javabean.jar . 
ENTRYPOINT /bin/bash -c "sleep 160; java -jar vorto-javabean.jar"
