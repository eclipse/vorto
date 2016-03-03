FROM java
VOLUME /tmp/generator/markdown
ADD target/vorto-markdown.jar vorto-markdown.jar
RUN bash -c 'touch vorto-markdown.jar'
ENTRYPOINT /bin/bash -c "sleep 30; java -jar vorto-markdown.jar --spring.profiles.active=eclipse"
