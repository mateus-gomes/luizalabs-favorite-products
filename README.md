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

## Running the application through IDE

If you want to run the application on the IDE make sure that the docker application is stopped and the environment variables are setted.

Here are the environment variables used in the project:

```
DATABASE_URL=jdbc:postgresql://localhost:5432/favorite-products;
DATABASE_USERNAME=postgres;
DATABASE_PASSWORD=luizalabs-interview;
JWT_SECRET_KEY=luizalabs
```