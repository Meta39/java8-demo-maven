ARG JDK_IMAGE
FROM $JDK_IMAGE

MAINTAINER Meta39

ARG APP_NAME
ADD ./target/$APP_NAME.jar /opt/arthas/app.jar

# JAVA参数，如-Xmx4g
ENV JAVA_OPTS="-Xms256m -Xmx256m"

# OPS参数，如 -Dskywalking.agent.service_name=hhis-core-app  -Dskywalking.collector.backend_service=0.0.0.0:80 \
ENV OPS_OPTS=""

# SPRING-BOOT参数，如--spring.config.location=/config/ --spring.config.additional-location=/global-config/
ENV BOOT_OPTS=""

# 运行jar包
ENTRYPOINT ["/bin/sh","-c","java ${JAVA_OPTS} ${OPS_OPTS} -jar /opt/arthas/app.jar ${BOOT_OPTS}"]


