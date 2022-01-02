CREATE TABLE `user`
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

CREATE TABLE `item`
(
  `id`           VARCHAR(32)  NOT NULL,
  `name`         VARCHAR(255) NOT NULL,
  `ingredient`   VARCHAR(255) NOT NULL,
  `size_info`    VARCHAR(255) NOT NULL DEFAULT '[]',
  `img_link`     VARCHAR(255) NOT NULL,
  `group`        VARCHAR(50)  NOT NULL,
  `is_sizable`   BOOLEAN NOT NULL,
  `item_id_seq`  VARCHAR(255) NOT NULL DEFAULT '[]',
  `price`        INT NULL,
  `created_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `coupon`
(
  `id`           VARCHAR(32) NOT NULL,
  `description`  VARCHAR(255) NULL,
  `expired_date` TIMESTAMP   NOT NULL,
  `effect_place` VARCHAR(20) NOT NULL,
  `value`        VARCHAR(255) NOT NULL,
  `unit`         VARCHAR(10) NULL,
  `created_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `cart`
(
  `id`          VARCHAR(32) NOT NULL,
  `item`        TEXT        NOT NULL,
  `coupon_id`   VARCHAR(32) NULL,
  `user_id`     VARCHAR(32) NOT NULL,
  `created_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `order`
(
  `id`               VARCHAR(32) NOT NULL,
  `item`             TEXT        NOT NULL,
  `coupon_id`        VARCHAR(32) NULL,
  `total_price`      INT NOT NULL,
  `status`           VARCHAR(50) NOT NULL,
  `user_id`          VARCHAR(32) NOT NULL,
  `created_at`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `order_customer_info`
(
  `order_id`   VARCHAR(32)  NOT NULL,
  `fullname`   VARCHAR(255) NOT NULL,
  `phone`      VARCHAR(18)  NOT NULL,
  `address`    VARCHAR(255) NOT NULL,
  `memo`       VARCHAR(255) NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4;
