DROP TABLE IF EXISTS products;

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(20) NOT NULL,
    title VARCHAR(255),
    category VARCHAR(255),
    image VARCHAR(255)
);
