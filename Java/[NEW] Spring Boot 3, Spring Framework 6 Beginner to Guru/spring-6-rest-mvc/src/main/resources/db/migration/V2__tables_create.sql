CREATE TABLE IF NOT EXISTS beer_order (
	id                 BINARY(16)             PRIMARY KEY,
	opt_lock           INT          NOT NULL,
	customer_ref       VARCHAR(255) NOT NULL,
	customer_id        BINARY(16)   NOT NULL,
	created_date       TIMESTAMP    NOT NULL,
	last_modified_date TIMESTAMP    NOT NULL,

	CONSTRAINT FK_CUSTOMER_ID_BEER_ORDER FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS beer_order_line (
	id                 BINARY(16)             PRIMARY KEY,
	opt_lock           INT          NOT NULL,
	beer_id			   BINARY(16)   NOT NULL,
	order_quantity     INT 			NOT NULL,
	quantity_allocated INT          NOT NULL,
	beer_order_id	   BINARY(16)   NOT NULL,
	created_date       TIMESTAMP    NOT NULL,
	last_modified_date TIMESTAMP    NOT NULL,

	CONSTRAINT FK_BEER_ID_BEER_ORDER_LINE       FOREIGN KEY (beer_id)       REFERENCES beer(id),
	CONSTRAINT FK_BEER_ORDER_ID_BEER_ORDER_LINE FOREIGN KEY (beer_order_id) REFERENCES beer_order(id)
);

CREATE TABLE category (
	id                 BINARY(16)   NOT NULL PRIMARY KEY,
	opt_lock           INT          NOT NULL,
    description        VARCHAR(255) NOT NULL,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);

CREATE TABLE beer_category (
    beer_id     BINARY(16)  NOT NULL,
    category_id BINARY(16)  NOT NULL,

    PRIMARY KEY (beer_id, category_id),

    CONSTRAINT FK_BEER_ID_BEER_CATEGORY     FOREIGN KEY (beer_id)     REFERENCES beer (id),
    CONSTRAINT FK_CATEGORY_ID_BEER_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE beer_order_shipment (
    id                 BINARY(16)  NOT NULL         PRIMARY KEY,
    opt_lock           INT         NOT NULL,
    tracking_number    VARCHAR(50) NOT NULL UNIQUE,
    created_date       TIMESTAMP   NOT NULL,
    last_modified_date TIMESTAMP   NOT NULL,

    CONSTRAINT FK_BEER_ORDER_ID_BEER_ORDER_SHIPMENT FOREIGN KEY (id) REFERENCES beer_order (id)
);