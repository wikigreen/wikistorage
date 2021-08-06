FROM openjdk:11
COPY ./build/libs/wikistorage-0.0.1-SNAPSHOT.jar wikistorage-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "wikistorage-0.0.1-SNAPSHOT.jar"]