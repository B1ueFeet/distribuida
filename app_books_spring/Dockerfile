# docker build -t app-books .

# docker login
# docker build -t b1uefeet/app-books-spring .
# docker push b1uefeet/app-books-spring

FROM eclipse-temurin:21.0.5_11-jre-alpine

RUN mkdir /app
WORKDIR /app

COPY build/libs/app_books_spring.jar app.jar

CMD ["java", "-jar", "app.jar"]