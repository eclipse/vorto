FROM java:8
VOLUME /tmp/generator/markdown
ADD target/vorto-markdown.jar vorto-markdown.jar
RUN bash -c 'touch vorto-markdown.jar'
ENTRYPOINT ["java","-jar","vorto-markdown.jar"]