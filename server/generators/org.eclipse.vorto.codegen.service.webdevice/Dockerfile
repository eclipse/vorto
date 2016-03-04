FROM java:8
VOLUME /tmp/generator/webdevice
ADD target/vorto-webdevice.jar vorto-webdevice.jar
RUN bash -c 'touch vorto-webdevice.jar'
ENTRYPOINT ["java","-jar","vorto-webdevice.jar"]