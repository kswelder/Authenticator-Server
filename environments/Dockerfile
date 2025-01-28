FROM openjdk:21-jdk

COPY ../target/Oauth-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
