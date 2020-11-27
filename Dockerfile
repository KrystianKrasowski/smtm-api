FROM java:8
COPY application/target/application-*-spring-boot.jar smtm-api.jar
ENTRYPOINT ["java", "-jar", "smtm-api.jar"]
