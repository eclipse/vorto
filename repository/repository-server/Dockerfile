FROM java:8
# Add folder to hold jar file
RUN mkdir /code
WORKDIR /code
ARG JAR_FILE
VOLUME /code
VOLUME /root/.vorto
RUN apt-get update && apt-get install -y jq sed
ADD ./target/${JAR_FILE} /code/infomodelrepository.jar
ADD ./docker/run.sh /code
RUN chmod +x run.sh
#Read mounted config file, remove newlines from file and write to variable, run spring boot after that
CMD ["/bin/bash", "/code/run.sh"]
EXPOSE 8080
