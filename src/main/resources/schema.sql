CREATE TABLE IF NOT EXISTS customers
  (
     id         UUID PRIMARY KEY,
     first_name VARCHAR (36) NOT NULL,
     last_name  VARCHAR (36) NOT NULL,
     address    VARCHAR (128) NOT NULL
  );

CREATE TABLE IF NOT EXISTS accounts
  (
     id           UUID PRIMARY KEY,
     country_code VARCHAR (3) NOT NULL,
     customer_id UUID,
     CONSTRAINT fk_customer FOREIGN KEY ( customer_id ) REFERENCES customers ( id )
  );

CREATE TABLE IF NOT EXISTS balances
  (
     id            UUID PRIMARY KEY,
     currency_code VARCHAR (36) NOT NULL,
     amount        DECIMAL NOT NULL,
     CONSTRAINT fk_account FOREIGN KEY ( account_id ) REFERENCES accounts ( id )
  );

CREATE TABLE IF NOT EXISTS transactions
  (
     id            UUID PRIMARY KEY,
     direction     VARCHAR (3) NOT NULL,
     currency_code VARCHAR (3) NOT NULL,
     description   VARCHAR (256) NOT NULL,
     amount        DECIMAL NOT NULL,
     created_at    TIMESTAMP NOT NULL,
     CONSTRAINT fk_balance FOREIGN KEY ( balance_id ) REFERENCES balances ( id )
  );