FROM docker.io/library/maven:3-eclipse-temurin-21-alpine  AS builder
WORKDIR /opt/account

COPY src/ src/
COPY pom.xml .

RUN mvn clean package

FROM gcr.io/distroless/java21-debian12:nonroot  AS runner

ENV TZ=America/Recife

WORKDIR /opt/account

EXPOSE 8082

COPY --from=builder /opt/account/target/account-*.jar /opt/account/account.jar

CMD ["/opt/account/account.jar","-Xms128m","-Xmx256m"]