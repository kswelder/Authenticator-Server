FROM openjdk:21-jdk

ENV USER_DB="kswelder" \
    PASS_DB="5544" \
    URL_DB="jdbc:postgresql://postgres_database" \
    PORT_DB="5432" \
    NAME_DB="blog" \
    WORK_DIR="/work" \
    PUB_KEY="/work/public_key.pem" \
    PRIV_KEY="/work/private_key.pem" \
    SPRING_PROFILES_ACTIVE="dev"

WORKDIR /work
WORKDIR /app

COPY ../target/Oauth-0.0.1-SNAPSHOT.jar app.jar
VOLUME /work

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
