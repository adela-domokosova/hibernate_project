CREATE SCHEMA MOJEAPP;
create user APP_USER password 'APP_USER';
USE MOJEAPP;
CREATE TABLE Member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    registration_date VARCHAR(20) NOT NULL
);


INSERT INTO Member (first_name, last_name, email, registration_date)
VALUES ('John', 'Doe', 'john.doe@example.com', '2025-05-01');

INSERT INTO Member (first_name, last_name, email, registration_date)
VALUES ('Jane', 'Smith', 'jane.smith@example.com', '2025-05-02');

INSERT INTO Member (first_name, last_name, email, registration_date)
VALUES ('Michael', 'Brown', 'michael.brown@example.com', '2025-05-03');

grant all on schema MOJEAPP to APP_USER;
ALTER TABLE Member
    ALTER COLUMN REGISTRATION_DATE SET DATA TYPE TIMESTAMP;