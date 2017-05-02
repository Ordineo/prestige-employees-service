FROM openjdk:8-jre
VOLUME /tmp
ADD prestige-employees-service-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ADD wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh
EXPOSE 8081 9901
