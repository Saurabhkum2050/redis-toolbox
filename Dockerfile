# Use AdoptOpenJDK 11 as the base image
FROM amazoncorretto:21-alpine-full

# Set environment variables
ENV SCALA_VERSION=2.13.14 \
    SBT_VERSION=1.10.0 \
    APP_NAME=RedisToolBox

# Install sbt
RUN wget -qO - "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" | tar xz -C /opt && \
    ln -s /opt/sbt/bin/sbt /usr/local/bin/sbt

# Set working directory
WORKDIR /app

# Copy project files
COPY ./target/scala-2.13/RedisToolBox.jar ./${APP_NAME}.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/RedisToolBox.jar"]
