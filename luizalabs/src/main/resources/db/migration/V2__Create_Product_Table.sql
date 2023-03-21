CREATE TABLE IF NOT EXISTS products (
    id_product UUID PRIMARY KEY,
    title varchar(80) NOT NULL,
    image varchar(255) NOT NULL,
    price decimal(9,2) NOT null,
    review decimal(3,2),
    fk_client UUID REFERENCES clients(id_client) ON DELETE CASCADE
);