FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=build/libs/calculator-api-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]