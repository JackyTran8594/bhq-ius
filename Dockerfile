
#### STAGE 1: MAVEN BUILD ###
#FROM maven:3.9.2-amazoncorretto-17-debian AS builders
## create app directory in images and copies pom.xml into it
#COPY pom.xml /app/
## copy all required dependencies into one layer
##RUN mvn -B dependency:resolve dependency:resolve-plugins
## copies source code into the app directort in image
#COPY src /app/src
## sets app as the directory into the app
#WORKDIR /app/
## run mvn
#RUN mvn clean install -Pprod


### STAGE 2: DEPLOY APPLICATION
FROM amazoncorretto:8-al2023
WORKDIR /app
COPY deploy/bhq-ius-0.0.1.jar /app/
RUN ls -la
ENTRYPOINT ["java","-jar", "bhq-ius-0.0.1.jar"]


