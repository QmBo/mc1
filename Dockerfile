FROM openjdk:11-jdk-oracle
WORKDIR mc1
ADD target/mc1.jar app.jar
ENTRYPOINT java -jar app.jar