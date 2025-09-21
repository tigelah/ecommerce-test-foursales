# === Build stage ===
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY ecommerce/.mvn .mvn
COPY ecommerce/mvnw .
COPY ecommerce/pom.xml .

#permissões e encoding
RUN chmod +x mvnw
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN ./mvnw -q -DskipTests dependency:go-offline
COPY ecommerce /src

RUN chmod +x mvnw && ./mvnw -q -DskipTests package

# === Runtime stage ===
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/target/*.jar /app/app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
