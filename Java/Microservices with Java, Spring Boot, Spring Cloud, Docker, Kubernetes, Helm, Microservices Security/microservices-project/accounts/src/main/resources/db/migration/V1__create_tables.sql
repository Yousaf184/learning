CREATE TABLE IF NOT EXISTS `customer` (
  `id`              BIGINT                 AUTO_INCREMENT  PRIMARY KEY,
  `name`            VARCHAR(100) NOT NULL,
  `email`           VARCHAR(100) NOT NULL,
  `mobile_number`   VARCHAR(20)  NOT NULL,
  `created_at`      DATETIME     NOT NULL,
  `created_by`      VARCHAR(50)  NOT NULL,
  `last_updated_at` DATETIME,
  `last_updated_by` VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS `account` (
  `account_number`  BIGINT                 AUTO_INCREMENT  PRIMARY KEY,
  `customer_id`     BIGINT       NOT NULL,
  `account_type`    VARCHAR(100) NOT NULL,
  `branch_address`  VARCHAR(200) NOT NULL,
  `created_at`      DATETIME     NOT NULL,
  `created_by`      VARCHAR(50)  NOT NULL,
  `last_updated_at` DATETIME,
  `last_updated_by` VARCHAR(50),

  CONSTRAINT FK_CUSTOMER_ID FOREIGN KEY (customer_id) REFERENCES customer(id)
);