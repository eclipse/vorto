SPRING_APPLICATION_JSON=$(jq .'"generators-example"' /gen/config/config.json | sed $'s/\r//' | tr -d '\n');
export SPRING_APPLICATION_JSON
java -jar /gen/generators.jar;
