# docker build -t app-books .

# docker login
# docker build -t aaguamaninga/app-books-spring .
# docker push aaguamaninga/app-books-spring

FROM eclipse-temurin:21.0.5_11-jre-alpine

RUN mkdir /app
WORKDIR /app

COPY build/libs/app_books_spring.jar app.jar

CMD ["java", "-jar", "app.jar"]