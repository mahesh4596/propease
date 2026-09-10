CREATE DATABASE javaproject;
USE javaproject;

SHOW DATABASES;

SHOW TABLES;
DESCRIBE properties;

CREATE TABLE customers (
    mobile VARCHAR(12) NOT NULL PRIMARY KEY,
    cname VARCHAR(30),
    address VARCHAR(100),
    city VARCHAR(15),
    email VARCHAR(50),
    ctype VARCHAR(30),
    pic MEDIUMBLOB,
    acard MEDIUMBLOB,
    doe DATE
);
SELECT * FROM customers;

CREATE TABLE properties (
    rid INT AUTO_INCREMENT PRIMARY KEY,
    mobile VARCHAR(12),
    location VARCHAR(100),
    area VARCHAR(100),
    city VARCHAR(50),
    size FLOAT,
    front FLOAT,
    rear FLOAT,
    lft FLOAT,
    rght FLOAT,
    facing VARCHAR(10),
    proptype VARCHAR(100),
    constype VARCHAR(100),
    approvedby VARCHAR(50),
    price VARCHAR(10),
    otherinfo VARCHAR(100),
    pic1 MEDIUMBLOB,
    pic2 MEDIUMBLOB,
    status VARCHAR(20) DEFAULT 'Available',
    CONSTRAINT fk_property_customer
        FOREIGN KEY (mobile)
        REFERENCES customers(mobile)
);
SELECT * FROM properties;

CREATE TABLE deals (
    dealid INT AUTO_INCREMENT PRIMARY KEY,
    rid INT NOT NULL,
    sellerMobile VARCHAR(12),
    buyerMobile VARCHAR(12),
    dealPrice VARCHAR(12) NOT NULL,
    advanceAmount VARCHAR(12) DEFAULT '0',
    balanceAmount VARCHAR(12) DEFAULT '0',
    commission VARCHAR(12) DEFAULT '0',
    dealDate DATE NOT NULL,
    registryDate DATE NOT NULL,
    dealStatus VARCHAR(30) DEFAULT 'Pending',
	FOREIGN KEY (rid) REFERENCES properties(rid),
    FOREIGN KEY (sellerMobile) REFERENCES customers(mobile),
	FOREIGN KEY (buyerMobile) REFERENCES customers(mobile)
);
SELECT * FROM deals;