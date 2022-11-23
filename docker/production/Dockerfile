FROM openjdk:17

WORKDIR /
RUN mkdir /app

COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
