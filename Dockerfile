FROM amazoncorretto:17-alpine-jdk AS build

COPY ./src  /bld/src/
COPY ./gradle /bld/gradle/
COPY build.gradle.kts settings.gradle.kts gradlew /bld/
RUN chmod +x /bld/gradlew
WORKDIR /bld
RUN ./gradlew --refresh-dependencies clean bootJar


FROM amazoncorretto:17-alpine AS app

COPY ./static-files /app/static-files/
COPY --from=build /bld/build/libs/publish-tests.jar /app/
RUN chmod +x /app/publish-tests.jar
WORKDIR /app
CMD ["java","-jar","/app/publish-tests.jar"]
