# docker build -t app-authors .
# docker build -t b1uefeet/app-authors-spring .
# docker push b1uefeet/app-authors-spring

FROM eclipse-temurin:21.0.5_11-jre-alpine

RUN mkdir /app
WORKDIR /app

COPY build/libs/app_authors_spring.jar app.jar

CMD ["java", "-jar", "app.jar"]