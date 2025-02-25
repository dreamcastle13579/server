FROM openjdk:17-alpine

ARG PORT=8080
EXPOSE ${PORT}

ARG JAR_FILE
ENV JAR_FILE=${JAR_FILE}

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]
