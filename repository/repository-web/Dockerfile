FROM java:8
# Add folder to hold jar file
ARG JAR_FILE
WORKDIR /code
VOLUME /root/.vorto
VOLUME /code
RUN apt-get update && apt-get install -y jq sed
ADD ./target/${JAR_FILE} /code/infomodelrepository.jar
ADD ./docker/run.sh /code
#Read mounted config file, remove newlines from file and write to variable, run spring boot after that
CMD ["/bin/bash", "/code/run.sh"]
EXPOSE 8080
