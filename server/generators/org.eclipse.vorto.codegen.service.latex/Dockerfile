FROM java
VOLUME /tmp/generator/latex
ADD target/vorto-latex.jar vorto-latex.jar
RUN bash -c 'touch vorto-latex.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-latex.jar --spring.profiles.active=eclipse"
