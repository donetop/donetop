FROM openjdk:11
LABEL authors="Jin (hjm5014@gmail.com)"

ARG PROFILE=docker
ENV PROFILE=${PROFILE}

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN adduser donetop
RUN mkdir -p /home/donetop/libs
RUN mkdir -p /home/donetop/storage

COPY build/libs/donetop-oss-0.0.1-SNAPSHOT.jar /home/donetop/libs/donetop-oss-0.0.1-SNAPSHOT.jar

CMD java -server -Dspring.profiles.active=$(echo ${PROFILE}) -Duser.timezone=KST -jar /home/donetop/libs/donetop-oss-0.0.1-SNAPSHOT.jar