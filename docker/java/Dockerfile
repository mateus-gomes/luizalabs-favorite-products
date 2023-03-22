FROM openjdk:17-jdk-alpine

COPY /luizalabs-*.jar /luizalabs.jar

ENV DATABASE_URL jdbc:postgresql://luizalabs-favorite-products-database-1:5432/favorite-products
ENV DATABASE_USERNAME postgres
ENV DATABASE_PASSWORD luizalabs-interview
ENV JWT_SECRET_KEY luizalabs

CMD ["java", "-jar", "/luizalabs.jar"]