CREATE TABLE IF NOT EXISTS users (
    id_user UUID PRIMARY KEY,
    username varchar(80) NOT NULL,
    full_name varchar(80) NOT NULL,
    password varchar(255) NOT NULL,
    account_non_locked boolean NOT NULL,
    account_non_expired boolean NOT NULL,
    credentials_non_expired boolean NOT NULL,
    enabled boolean NOT NULL
);