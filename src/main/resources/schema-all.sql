DROP TABLE products IF EXISTS;

CREATE TABLE products (
    id VARCHAR(20) NOT NULL,
    title VARCHAR(100),
    category VARCHAR(100),
    image VARCHAR(100)
);

CREATE TABLE product_categories (
    id VARCHAR(20) NOT NULL,
    category VARCHAR(100)
);

CREATE TABLE product_imageURLs (
    id VARCHAR(20) NOT NULL,
    imageURL VARCHAR(100)
);
