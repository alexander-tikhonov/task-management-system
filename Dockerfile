FROM maven:3.8.1-openjdk-17
COPY /core /app/core
COPY pom.xml /app
WORKDIR /app/core
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]