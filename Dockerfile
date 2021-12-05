FROM java:8-jdk-alpine
COPY ./target/dagacube-service-1.0.0-SNAPSHOT.jar /usr/dagacube-service/
WORKDIR /usr/dagacube-service
ENTRYPOINT ["java","-jar","dagacube-service-1.0.0-SNAPSHOT.jar"]