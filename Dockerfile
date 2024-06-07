FROM openjdk:17
EXPOSE 8091
ADD target/*.jar ./app.jar
ENTRYPOINT ["java","-jar","app.jar"]
