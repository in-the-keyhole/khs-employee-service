FROM khsprojectuiservice_service-base
RUN mkdir --parents /usr/src/app
WORKDIR /usr/src/app
ADD /pom.xml /container/app/
RUN mvn verify clean --fail-never
ADD . /usr/src/app
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8082
ENTRYPOINT ["java","-jar","target/khs-employee-service-app.jar"]
