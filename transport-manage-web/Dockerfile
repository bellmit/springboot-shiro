FROM registry.01zhuanche.com/devops/jdk1.8_spring_boot:v0.4

MAINTAINER zhaojingwei@01zhuanche.com

#mkdir code
RUN mkdir -p /u01/transport-manage-gateway

# add code
ADD transport-manage-web/run.sh trans/target/transport-manage-web.jar /u01/transport-manage-gateway/

# chmod
RUN chmod 775 /u01/transport-manage-gateway/run.sh && \
    chmod +x  /u01/transport-manage-gateway/run.sh

# entrypoint
ENTRYPOINT /u01/transport-manage-gateway/run.sh