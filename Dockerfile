FROM amazoncorretto:21-alpine

ENV SCALA_VERSION=2.13.14 \
    SBT_VERSION=1.10.0 \
    APP_NAME=RedisToolBox

WORKDIR /app

COPY ./target/scala-2.13/RedisToolBox.jar ./${APP_NAME}.jar

ENTRYPOINT ["java", "-jar", "/app/RedisToolBox.jar"]
