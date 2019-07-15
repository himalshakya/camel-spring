CREATE TABLE ITEMS (
  ITEM_I SERIAL,
  SKU TEXT NOT NULL,
  ITEM_DESCRIPTION TEXT DEFAULT NULL,
  PRICE NUMERIC(5,2),
  CRTE_TS TIMESTAMPTZ NULL DEFAULT current_timestamp
);


SELECT * FROM ITEMS;

create user cameluser with encrypted password 'camelpassword';

grant all privileges on database cameldb to cameluser;

SELECT * FROM ITEMS


GRANT ALL PRIVILEGES ON TABLE items TO cameluser;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public to cameluser;

GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public to cameluser;