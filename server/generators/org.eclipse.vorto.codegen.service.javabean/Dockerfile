FROM java:8
VOLUME /tmp/generator/javabean
ADD target/vorto-javabean.jar vorto-javabean.jar
RUN bash -c 'touch vorto-javabean.jar'
ENTRYPOINT ["java","-jar","vorto-javabean.jar"]