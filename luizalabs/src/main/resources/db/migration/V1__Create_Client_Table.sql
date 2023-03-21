CREATE TABLE IF NOT EXISTS clients (
    id_client UUID PRIMARY KEY,
    client_email varchar(80) NOT NULL,
    client_name varchar(80) NOT NULL
);