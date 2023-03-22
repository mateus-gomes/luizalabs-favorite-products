# Luizalabs - Wishlist service
Service responsible for managing clients registries and client's wishlist

## Prerequisites
- Docker

## Setup
The first thing to do as you clone the repository is to setup the database and application docker. In order to do it run the following command:

```
docker compose up -d
```

Now the application is already up!

## Swagger
If you want to explore informations about the API endpoints you can access the swagger by accessing http://localhost:8080/swagger-ui/index.html when the application is up.

## Endpoint collection
If you want to test the endpoints through some API plataform, in the base path of the project there is a file named [collection.json](https://github.com/mateus-gomes/luizalabs-favorite-products/blob/main/collection.json), you can import it inside your API plataform and it will have all the application endpoints.

## Running the application through IDE
If you want to run the application on the IDE make sure that the docker application is stopped and the environment variables are setted.

Here are the environment variables used in the project:

```
DATABASE_URL=jdbc:postgresql://localhost:5432/favorite-products;
DATABASE_USERNAME=postgres;
DATABASE_PASSWORD=luizalabs-interview;
JWT_SECRET_KEY=luizalabs
```

## Running the application through terminal
### Prerequisites
- [gradle](https://gradle.org/install/)
- [Java 17+](https://www.java.com/pt-BR/download)

If you want to run the application on the terminal make sure that both the docker and the IDE application are not running.

### Step by step
1. Add the environment variables to your system. You can use the .env file. In your terminal:
```
source .env
```

2. Run the docker with the database. In your terminal:
```
docker compose up -d database
```

3. Start the application. In the root folder of the repository access the _luizalabs_ folder. In the terminal:
```
cd luizalabs
```

Inside this folder run the start command. In terminal:
```
gradle bootRun
```

If you can see the message bellow, it means the application is ready to use:

**Started LuizalabsApplication in x seconds (process running for x)**

## Running tests through terminal
### Prerequisites
- [gradle](https://gradle.org/install/)
- [Java 17+](https://www.java.com/pt-BR/download)

### Step by step
1. Add the environment variables to your system. You can use the .env file. In your terminal:
```
source .env
```
2. Run the docker with the database. In your terminal:
```
docker compose up -d database
```
3. Run the gradle test task. In your terminal:
```
gradle test
```