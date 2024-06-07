FROM openjdk:17
EXPOSE 8091
ADD target/now:0.0.1-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java","-jar","app.jar"]
