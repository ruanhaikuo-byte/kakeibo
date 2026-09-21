CREATE TABLE users (
                       id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(100) NOT NULL,
                       created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE categories (
                            id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id    BIGINT NOT NULL,
                            name       VARCHAR(30) NOT NULL,
                            type       VARCHAR(10) NOT NULL,
                            sort_order INT NOT NULL DEFAULT 0,
                            UNIQUE (user_id, name, type),
                            CHECK (type IN ('INCOME', 'EXPENSE')),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE transactions (
                              id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id          BIGINT NOT NULL,
                              category_id      BIGINT NOT NULL,
                              amount           BIGINT NOT NULL,
                              transaction_date DATE NOT NULL,
                              memo             VARCHAR(100),
                              created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CHECK (amount > 0),
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                              FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
                              INDEX idx_transactions_user_date (user_id, transaction_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;