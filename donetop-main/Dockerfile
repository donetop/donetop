FROM openjdk:11
LABEL authors="Jin (hjm5014@gmail.com)"

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN adduser donetop
RUN mkdir -p /home/donetop/libs
RUN mkdir -p /home/donetop/storage

COPY build/libs/donetop-main-0.0.1-SNAPSHOT.jar /home/donetop/libs/donetop-main-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-server", "-Dspring.profiles.active=docker", "-Duser.timezone=KST", "-jar", "/home/donetop/libs/donetop-main-0.0.1-SNAPSHOT.jar"]