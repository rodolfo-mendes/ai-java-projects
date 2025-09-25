FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY build/libs/app.jar app.jar

EXPOSE 8080

# Pass environment variable at runtime; Spring Boot will read it
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

