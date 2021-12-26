CREATE TABLE `users`
(
  `id`         VARCHAR(32)  NOT NULL,
  `username`   VARCHAR(255) NOT NULL UNIQUE,
  `email`      VARCHAR(255) NOT NULL UNIQUE,
  `password`   VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `items`
(
  `id`           VARCHAR(32)  NOT NULL,
  `name`         VARCHAR(255) NOT NULL,
  `ingredient`   VARCHAR(255) NOT NULL,
  `extra_text`   VARCHAR(255) NULL,
  `img_link`     VARCHAR(255) NOT NULL,
  `group`        VARCHAR(50)  NOT NULL,
  `is_sizable`   BOOLEAN NOT NULL,
  `is_combo`     BOOLEAN NOT NULL,
  `item_id_seq`  TEXT NOT NULL,
  `price`        INT NULL,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `coupons`
(
  `id`           VARCHAR(32) NOT NULL,
  `description`  VARCHAR(255) NULL,
  `expired_date` TIMESTAMP   NOT NULL,
  `effect_place` VARCHAR(10) NOT NULL,
  `is_money`     BOOLEAN NOT NULL,
  `value`        VARCHAR(32) NOT NULL,
  `unit`         VARCHAR(10) NULL,
  `created_at`   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `carts`
(
  `id`          VARCHAR(32) NOT NULL,
  `item_id_seq` TEXT        NOT NULL,
  `coupon_id`   VARCHAR(32) NULL,
  `user_id`     VARCHAR(32) NOT NULL,
  `created_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `orders`
(
  `id`               VARCHAR(32) NOT NULL,
  `item_id_seq`      TEXT        NOT NULL,
  `coupon_id`        VARCHAR(32) NULL,
  `customer_info_id` VARCHAR(32) NOT NULL,
  `total_price`      INT NOT NULL,
  `status`           VARCHAR(50) NOT NULL,
  `user_id`          VARCHAR(32) NOT NULL,
  `created_at`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `order_customer_info`
(
  `id`         VARCHAR(32)  NOT NULL,
  `full_name`  VARCHAR(255) NOT NULL,
  `phone`      VARCHAR(18)  NOT NULL,
  `address`    VARCHAR(255) NOT NULL,
  `memo`       VARCHAR(255) NULL,
  `order_id`   VARCHAR(32)  NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;
