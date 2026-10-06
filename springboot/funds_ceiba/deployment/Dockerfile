FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY *.jar ceiba_prueba_tecnica.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
USER appuser
ENTRYPOINT [ "/bin/sh", "-c", "/opt/java/openjdk/bin/java $JAVA_OPTS  -jar /ceiba_prueba_tecnica.jar" ]
