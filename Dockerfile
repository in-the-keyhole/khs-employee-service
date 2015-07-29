FROM khs-service-base

RUN mkdir --parents /usr/src/app
WORKDIR /usr/src/app

ADD . /usr/src/app

RUN mvn install

EXPOSE 8082
CMD java -jar target/khs-resource-service-app.jar
