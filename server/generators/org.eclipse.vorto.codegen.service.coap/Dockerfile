FROM java:8
VOLUME /tmp/generator/coap
ADD target/vorto-coap.jar vorto-coap.jar
RUN bash -c 'touch vorto-coap.jar'
ENTRYPOINT ["java","-jar","vorto-coap.jar"]