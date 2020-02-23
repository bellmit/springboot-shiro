FROM registry.01zhuanche.com/devops/jdk1.8_spring_boot:v0.4

MAINTAINER suchao@01zhuanche.com

#mkdir code
RUN mkdir -p /u01/mp-conference-manage

# add code
ADD conference-manage-api/run.sh conference-manage-api/target/conference-manage-api.jar /u01/mp-conference-manage/

# chmod
RUN chmod 775 /u01/mp-conference-manage/run.sh && \
    chmod +x  /u01/mp-conference-manage/run.sh

# entrypoint
ENTRYPOINT /u01/mp-conference-manage/run.sh