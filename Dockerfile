FROM maven:3.3.3-jdk-8
RUN mkdir --parents /usr/src/app
WORKDIR /usr/src/app
ADD /pom.xml /usr/src/app/
RUN mvn verify clean --fail-never
ADD . /usr/src/app
RUN mvn clean package -Dmaven.test.skip=true
EXPOSE 8082
ENTRYPOINT ["java","-jar","target/khs-employee-service-app.jar"]
