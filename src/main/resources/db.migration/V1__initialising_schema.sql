CREATE TABLE exchange_rates (
                          id SERIAL PRIMARY KEY,
                          currency_id INTEGER NOT NULL,
                          name VARCHAR (50) NOT NULL,
                          code VARCHAR (3) NOT NULL,
                          rate INTEGER NOT NULL,
                          exchange_date TIMESTAMP,
                          last_updated TIMESTAMP
);