CREATE TABLE IF NOT EXISTS `card` (
    `card_id`          BIGINT       NOT NULL  AUTO_INCREMENT PRIMARY KEY,
    `mobile_number`    VARCHAR(15)  NOT NULL,
    `card_number`      VARCHAR(100) NOT NULL,
    `card_type`        VARCHAR(100) NOT NULL,
    `total_limit`      INT          NOT NULL,
    `amount_used`      INT          NOT NULL,
    `available_amount` INT          NOT NULL,
    `created_at`       DATETIME     NOT NULL,
    `created_by`       VARCHAR(20)  NOT NULL,
    `updated_at`       DATETIME,
    `updated_by`       VARCHAR(20)  DEFAULT NULL
);