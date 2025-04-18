CREATE TABLE IF NOT EXISTS beer (
    id                 INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    beer_name          VARCHAR(255),
    beer_style         VARCHAR(255),
    upc                VARCHAR(25),
    quantity_on_hand   INTEGER,
    price              DECIMAL,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP
);

CREATE TABLE if NOT EXISTS customer (
    id                 INTEGER      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name               VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP
);