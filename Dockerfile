FROM openjdk:8
MAINTAINER Jaime Niswonger <jniswonger@keyholesoftware.com>

ENV BOOTAPP_JAVA_OPTS -Xms256m -Xmx256m

ENV BOOTAPP_USR bootapp
ENV BOOTAPP_GROUP bootapp_group
ENV BOOTAPP_PATH /app.jar
ENV BOOTAPP_DATA_VOLUME /data
ENV SERVER_PORT 8082

COPY wrapper.sh /wrapper.sh

RUN groupadd -r $BOOTAPP_GROUP && useradd -r -g $BOOTAPP_GROUP $BOOTAPP_USR \
	&& chmod 555 /wrapper.sh

EXPOSE $SERVER_PORT

USER $BOOTAPP_USR

USER root
COPY target/khs-employee-service.jar $BOOTAPP_PATH
RUN chmod 555 $BOOTAPP_PATH && \
            touch $BOOTAPP_PATH
USER $BOOTAPP_USR

ENTRYPOINT ["/wrapper.sh"]