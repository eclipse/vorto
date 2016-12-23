FROM java:8
VOLUME /tmp/generator/aws
ADD target/vorto-aws.jar .
ENTRYPOINT /bin/bash -c "sleep 140; java -jar vorto-aws.jar"
