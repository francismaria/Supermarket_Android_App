PRAGMA foreign_keys=ON;
BEGIN TRANSACTION;

.headers on
.mode column

DROP TABLE IF EXISTS CARDS;
CREATE TABLE CARDS(
  ID        INTEGER PRIMARY KEY,
  NUMBER    TEXT UNIQUE NOT NULL,
  EXP_DATE  TEXT NOT NULL CHECK(length(EXP_DATE) = 5),
  CCV       TEXT NOT NULL CHECK(length(CCV) = 3)
);

INSERT INTO CARDS VALUES(1, "123X-1234-5432-XYZW", "09/22", "223");

DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(
  UUID        INTEGER     PRIMARY KEY,
  NAME        TEXT        NOT NULL,
  EMAIL       VARCHAR(60) UNIQUE NOT NULL,
  USERNAME    TEXT UNIQUE NOT NULL CHECK(length(USERNAME) >= 6 AND length(USERNAME) <= 30),
  PASSWORD    TEXT NOT NULL CHECK(length(PASSWORD) >= 6 AND length(PASSWORD) <= 30),
  CARD_ID     INTEGER     NOT NULL,
  PUBLIC_KEY  BLOB        NOT NULL,
  FOREIGN KEY(CARD_ID) REFERENCES CARDS(ID) ON DELETE CASCADE
);

INSERT INTO USERS VALUES(1, "Francisco Maria", "f@gmail.com", "francis", "password", 1, "llaskdçaskdçlaksçldkaçl");

DROP TABLE IF EXISTS TRANSACTIONS;
CREATE TABLE TRANSACTIONS(
  ID      INTEGER   PRIMARY KEY,
  USER_ID INTEGER   NOT NULL,
  TIME    TEXT      NOT NULL,
  FOREIGN KEY(USER_ID) REFERENCES USERS(UUID)
);

SELECT * FROM CARDS INNER JOIN USERS INNER JOIN TRANSACTIONS;

COMMIT;
