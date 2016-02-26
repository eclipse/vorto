FROM java:8
VOLUME /tmp/generator/latex
ADD target/vorto-latex.jar vorto-latex.jar
RUN bash -c 'touch vorto-latex.jar'
ENTRYPOINT ["java","-jar","vorto-latex.jar"]