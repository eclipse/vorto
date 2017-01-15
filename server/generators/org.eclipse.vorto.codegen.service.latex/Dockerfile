FROM java:8
VOLUME /tmp/generator/latex
ADD target/vorto-latex.jar .
ENTRYPOINT /bin/bash -c "sleep 240; java -jar vorto-latex.jar"
