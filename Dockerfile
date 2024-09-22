FROM openjdk:17
ARG JAR_FILE=application-spring/build/libs/application-spring.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
