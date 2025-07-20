CREATE TABLE IF NOT EXISTS `loan` (
    `loan_id`            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `mobile_number`      VARCHAR(15)  NOT NULL,
    `loan_number`        VARCHAR(100) NOT NULL,
    `loan_type`          VARCHAR(100) NOT NULL,
    `total_loan`         INT          NOT NULL,
    `amount_paid`        INT          NOT NULL,
    `outstanding_amount` INT          NOT NULL,
    `created_at`         DATETIME     NOT NULL,
    `created_by`         VARCHAR(20)  NOT NULL,
    `updated_at`         DATETIME,
    `updated_by`         VARCHAR(20)
);