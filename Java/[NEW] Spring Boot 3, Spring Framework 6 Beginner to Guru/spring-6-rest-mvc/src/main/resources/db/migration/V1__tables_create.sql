CREATE TABLE IF NOT EXISTS beer (
	id			     BINARY(16)              PRIMARY KEY,
	opt_lock         INT           NOT NULL,
	beer_name        VARCHAR(255)  NOT NULL,
	beer_style       VARCHAR(255)  NOT NULL,
	upc              VARCHAR(255)  NOT NULL,
	quantity_on_hand INT           NOT NULL,
	price            DECIMAL(6, 2) NOT NULL,
	created_date     TIMESTAMP     NOT NULL,
	updated_date     TIMESTAMP     NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
	id			     BINARY(16)              PRIMARY KEY,
	opt_lock         INT           NOT NULL,
	name	         VARCHAR(255)  NOT NULL,
	created_date     TIMESTAMP     NOT NULL,
	updated_date     TIMESTAMP     NOT NULL
);