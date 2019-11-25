FROM openjdk:11

EXPOSE 8080

ADD ./build/libs/tastamat-api-fat.jar /opt/tastamat-api-fat.jar

WORKDIR /opt

RUN mkdir conf

# COPY ./conf /opt/conf

ENTRYPOINT ["java", "-jar", "/opt/tastamat-api-fat.jar"]
