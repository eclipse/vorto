FROM java:8
VOLUME /tmp/generator/ios
ADD target/vorto-ios.jar vorto-ios.jar
RUN bash -c 'touch vorto-ios.jar'
ENTRYPOINT ["java","-jar","vorto-ios.jar"]