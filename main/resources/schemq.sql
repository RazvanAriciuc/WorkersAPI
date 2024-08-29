CREATE DATABASE workers_db;

CREATE TABLE workers(
    worker_id INT PRIMARY KEY AUTO_INCREMENT,
    age INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    workplace VARCHAR(50),
    city VARCHAR(25),
    country VARCHAR(25),
    birthDate DATE
);

ALTER TABLE workers
RENAME COLUMN workplace TO workPlace;

INSERT INTO workers
VALUES  (1, 24, "Spongebob", "Squarepants", "Krusty's", "Bikini Bottom", "Atlantis", "2000-01-01"),
        (2, 21, "Patrick", "Star", "Krusty's", "Bikini Bottom", "Atlantis", "2002-04-09"),
        (3, 23, "Bruce", "Wayne", "Wayne Enterprise", "Gotham", "USA", "2001-10-29"),
        (4, 35, "Jack", "Sparrow", "Pirate Ship", "Unknown", "Caribbeans", "1775-02-24"),
        (5, 44, "Peter", "Parker", "Daily Mail", "New York", "USA", "1985-06-02"),
        (6, 25, "Tony", "Stark", "Stark Industries", "California", "USA", "2003-04-20"),;
