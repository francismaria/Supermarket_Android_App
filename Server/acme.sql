PRAGMA foreign_keys=ON;
BEGIN TRANSACTION;

.headers on
.mode column

/* ------------------------------ *
 *             CARDS              *
 * ------------------------------ */

DROP TABLE IF EXISTS CARDS;
CREATE TABLE CARDS(
  ID        INTEGER PRIMARY KEY,
  NUMBER    TEXT UNIQUE NOT NULL,
  EXP_DATE  TEXT NOT NULL CHECK(length(EXP_DATE) = 5),
  CCV       TEXT NOT NULL CHECK(length(CCV) = 3)
);

INSERT INTO CARDS VALUES(1, "123X-1234-5432-XYZW", "09/22", "223");

/* ------------------------------ *
 *             USERS              *
 * ------------------------------ */

DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(
  UUID              INTEGER     PRIMARY KEY,
  NAME              TEXT        NOT NULL,
  EMAIL             VARCHAR(60) UNIQUE NOT NULL,
  USERNAME          TEXT UNIQUE NOT NULL CHECK(length(USERNAME) >= 6 AND length(USERNAME) <= 30),
  PASSWORD          TEXT NOT NULL,
  CARD_ID           INTEGER     NOT NULL,
  PUBLIC_KEY        BLOB        NOT NULL,
  ACCUMULATED_CASH  REAL DEFAULT 0.0,
  VOUCHERS_AVLB     INTEGER DEFAULT 0,
  FOREIGN KEY(CARD_ID) REFERENCES CARDS(ID) ON DELETE CASCADE
);

INSERT INTO USERS(UUID, NAME, EMAIL, USERNAME, PASSWORD, CARD_ID, PUBLIC_KEY) VALUES(1, "Francisco Maria", "f@gmail.com", "francis", "password", 1, "llaskdçaskdçlaksçldkaçl");


/* ------------------------------ *
 *           PRODUCTS             *
 * ------------------------------ */

DROP TABLE IF EXISTS PRODUCTS;
CREATE TABLE PRODUCTS(
  UUID    INTEGER PRIMARY KEY,
  PRICE   REAL    NOT NULL,
  NAME    TEXT    NOT NULL,
  STOCK   INTEGER NOT NULL
);

INSERT INTO PRODUCTS VALUES(1, 14.00, 'SHOWER', 5);
INSERT INTO PRODUCTS VALUES(2, 20.00, 'BATH', 5);

/* ------------------------------ *
 *          TRANSACTIONS          *
 * ------------------------------ */

DROP TABLE IF EXISTS TRANSACTIONS;
CREATE TABLE TRANSACTIONS(
  ID         INTEGER   PRIMARY KEY,
  USER_ID    INTEGER   NOT NULL,
  DATE       TEXT      NOT NULL,
  VOUCHERS   INTEGER NOT NULL,
  TOTAL_COST REAL DEFAULT 0.0,
  FOREIGN KEY(USER_ID) REFERENCES USERS(UUID) ON DELETE CASCADE
);

INSERT INTO TRANSACTIONS (ID, USER_ID, DATE, VOUCHERS) VALUES(1, 1, "26/04/2019", 0);
INSERT INTO TRANSACTIONS (ID, USER_ID, DATE, VOUCHERS) VALUES(2, 1, "27/04/2019", 0);

/* ------------------------------ *
 *            HISTORY             *
 * ------------------------------ */

DROP TABLE IF EXISTS HISTORY;
CREATE TABLE HISTORY(
  TRANSACTION_ID  INTEGER NOT NULL,
  PRODUCT_ID      INTEGER NOT NULL,
  QUANTITY        INTEGER NOT NULL,
  FOREIGN KEY(TRANSACTION_ID) REFERENCES TRANSACTIONS(ID) ON DELETE CASCADE,
  FOREIGN KEY(PRODUCT_ID) REFERENCES PRODUCTS(UUID) ON DELETE CASCADE,
  PRIMARY KEY(TRANSACTION_ID, PRODUCT_ID)
);

-- TODO

-- TRIGGER : Add voucher if multiple of 100 is reached
-- TRIGGER : Update product stock

-- TRIGGER : Updates the total cost of the transaction upon insertion of a new product to the history

CREATE TRIGGER update_transaction_total_cost
  AFTER INSERT ON HISTORY
    BEGIN
      UPDATE TRANSACTIONS SET TOTAL_COST = TOTAL_COST + NEW.QUANTITY * (SELECT PRICE FROM PRODUCTS WHERE NEW.PRODUCT_ID = PRODUCTS.UUID)
        WHERE TRANSACTIONS.ID = NEW.TRANSACTION_ID;
          END; 

-- TRIGGER : Updates accumulated cash on USER

CREATE TRIGGER update_accumulated_cash
  AFTER INSERT ON HISTORY
      BEGIN
        UPDATE USERS SET ACCUMULATED_CASH = ACCUMULATED_CASH + NEW.QUANTITY 
          WHERE USERS.UUID = (SELECT user.UUID FROM USERS as user WHERE user.UUID = (SELECT trans.USER_ID FROM TRANSACTIONS as trans WHERE trans.ID = NEW.TRANSACTION_ID));
            END;


INSERT INTO HISTORY VALUES(1, 1, 10);
INSERT INTO HISTORY VALUES(1, 2, 5);
INSERT INTO HISTORY VALUES(2, 2, 1);
INSERT INTO HISTORY VALUES(2, 1, 1);


SELECT * FROM USERS;
SELECT * FROM PRODUCTS;
SELECT * FROM TRANSACTIONS;
SELECT * FROM HISTORY;

SELECT TRANSACTION_ID, PRODUCT_ID, QUANTITY, TOTAL_COST, DATE, USER_ID FROM HISTORY INNER JOIN TRANSACTIONS;


COMMIT;
