INSERT INTO CUSTOMERS (id, first_name, last_name, address) VALUES ('713024c6-615f-466c-9a14-95ba2f147cd3', 'first_name', 'last_name', 'address') ON CONFLICT DO NOTHING;
INSERT INTO ACCOUNTS (id, country_code, customer_id) VALUES ('713024c6-615f-466c-9a14-95ba2f147cd4', 'EST', '713024c6-615f-466c-9a14-95ba2f147cd3') ON CONFLICT DO NOTHING;
INSERT INTO BALANCES (id, currency_code, amount, account_id) VALUES ('713024c6-615f-466c-9a14-95ba2f147cd5', 'EUR', 1, '713024c6-615f-466c-9a14-95ba2f147cd4') ON CONFLICT DO NOTHING;
INSERT INTO TRANSACTIONS (id, direction, currency_code, description, amount, created_at, balance_id) VALUES ('713024c6-615f-466c-9a14-95ba2f147cd6', 'IN', 'EUR', 'A Big transaction', 99.99, CURRENT_TIMESTAMP, '713024c6-615f-466c-9a14-95ba2f147cd5') ON CONFLICT DO NOTHING;
