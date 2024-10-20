FROM eclipse-temurin:17-jdk

MAINTAINER com.j2zromero

COPY target/PointOfSale-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

