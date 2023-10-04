CREATE DATABASE testdb;
use testdb;

CREATE TABLE tatbestand (
    id MEDIUMINT NOT NULL AUTO_INCREMENT,
    tattag DATE,
    tatzeit TIME,
    tatort VARCHAR(100),
    tatort2 VARCHAR(100),
    tatbestand INT,
    betrag DOUBLE,
    PRIMARY KEY(id)
);


CREATE TABLE user (
    id MEDIUMINT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
	email VARCHAR(100),
	gender VARCHAR(100),
	ip_address VARCHAR(100),
	country_code VARCHAR(100),
    PRIMARY KEY(id)
);