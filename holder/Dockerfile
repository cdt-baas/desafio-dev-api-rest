FROM docker.io/library/maven:3-eclipse-temurin-21-alpine  AS builder
WORKDIR /opt/holder

COPY src/ src/
COPY pom.xml .

RUN mvn clean package

FROM gcr.io/distroless/java21-debian12:nonroot  AS runner

ENV TZ=America/Recife

WORKDIR /opt/holder

EXPOSE 8081

COPY --from=builder /opt/holder/target/holder-*.jar /opt/holder/holder.jar

CMD ["/opt/holder/holder.jar","-Xms128m","-Xmx256m"]