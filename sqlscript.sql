CREATE SCHEMA MOJEAPP;
create user APP_USER password 'APP_USER';
USE MOJEAPP;
CREATE TABLE member (
                        member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        registration_date TIMESTAMP NOT NULL
);

CREATE TABLE subscription (
                              subscription_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              subscription_type VARCHAR(255) NOT NULL,
                              price DOUBLE NOT NULL,
                              member_id BIGINT NOT NULL,
                              start_date DATE NOT NULL,
                              created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              active BOOLEAN DEFAULT FALSE NOT NULL,
                              CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE payment (
                         payment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         payment_date DATE NOT NULL,
                         amount DOUBLE NOT NULL,
                         subscription_id BIGINT UNIQUE,
                         CONSTRAINT fk_subscription FOREIGN KEY (subscription_id) REFERENCES subscription(subscription_id)
);

INSERT INTO member (first_name, last_name, email, registration_date)
VALUES
    ('Jan', 'Novák', 'jan.novak@example.com', CURRENT_TIMESTAMP),
    ('Eva', 'Krejčí', 'eva.krejci@example.com', CURRENT_TIMESTAMP);

INSERT INTO subscription (subscription_type, price, member_id, start_date, created_date, active)
VALUES
    ('WEEKLY', 199.99, 1, CURRENT_DATE, CURRENT_TIMESTAMP, FALSE),
    ('MONTHLY', 299.99, 2, CURRENT_DATE, CURRENT_TIMESTAMP, FALSE);


INSERT INTO payment (payment_date, amount, subscription_id)
VALUES (CURRENT_DATE, 199.99, 1);

-- Ruční aktivace subscription (protože trigger není dostupný v H2)
UPDATE subscription SET active = TRUE WHERE subscription_id = 1;

grant all on schema MOJEAPP to APP_USER;
