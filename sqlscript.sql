CREATE SCHEMA MOJEAPP;
create user APP_USER password 'APP_USER';
USE MOJEAPP;
CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    registration_date TIMESTAMP NOT NULL
);
CREATE TABLE subscription (
   subscription_id BIGINT AUTO_INCREMENT PRIMARY KEY,
   subscription_type VARCHAR(255) NOT NULL,
   price DOUBLE NOT NULL
);
CREATE TABLE payment (
     payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     member_id BIGINT NOT NULL,
     subscription_id BIGINT NOT NULL,
     payment_date DATE NOT NULL,
     amount DOUBLE NOT NULL,
     CONSTRAINT FK_Member FOREIGN KEY (member_id) REFERENCES member(member_id),
     CONSTRAINT FK_Subscription FOREIGN KEY (subscription_id) REFERENCES subscription(subscription_id)
);



INSERT INTO member (first_name, last_name, email, registration_date)
VALUES ('John', 'Doe', 'john.doe@example.com', '2025-05-01');

INSERT INTO member (first_name, last_name, email, registration_date)
VALUES ('Jane', 'Smith', 'jane.smith@example.com', '2025-05-02');

INSERT INTO member (first_name, last_name, email, registration_date)
VALUES ('Michael', 'Brown', 'michael.brown@example.com', '2025-05-03');

INSERT INTO member (FIRST_NAME, LAST_NAME, EMAIL, REGISTRATION_DATE) VALUES
         ('John', 'Doe', 'john.doeque@example.com', CURRENT_TIMESTAMP),
         ('Jane', 'Smith', 'jane.smithova@example.com', CURRENT_TIMESTAMP);

INSERT INTO subscription (subscription_type, price) VALUES
         ('WEEKLY', 1.99),
         ('MONTHLY', 9.99),
         ('YEARLY', 19.99);

INSERT INTO payment (member_id, subscription_id, payment_date, amount) VALUES
          (1, 1, '2025-05-19', 1.00),
          (1, 1, '2025-05-19', 0.99),
          (1, 2, '2025-05-19', 9.99),
          (2, 1, '2025-05-19', 1.99),
            (2, 1, '2025-05-19', 1.99),
          (4, 3, '2025-05-19', 19.99);

grant all on schema MOJEAPP to APP_USER;
