DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS REVIEW;
DROP TABLE IF EXISTS RESTAURANT;

CREATE TABLE RESTAURANT (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  NAME VARCHAR(100) NOT NULL,
  ADDRESS VARCHAR(250) NOT NULL,
  CITY VARCHAR(100) NOT NULL
);

CREATE TABLE REVIEW (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  MESSAGE VARCHAR(500) NOT NULL,
  STARS INT(1) NOT NULL,
  RESTAURANT_ID INT NOT NULL,
  CONSTRAINT `RESTAURANT_REVIEW_FK` FOREIGN KEY (`RESTAURANT_ID`) REFERENCES `RESTAURANT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE COMMENT (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  MESSAGE VARCHAR(500) NOT NULL,
  REVIEW_ID INT NOT NULL,
  CONSTRAINT `COMMENT_REVIEW_FK` FOREIGN KEY (`REVIEW_ID`) REFERENCES `REVIEW` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO RESTAURANT (ID, NAME, ADDRESS, CITY) VALUES
  (1, 'Da Luigi', 'Via Verdi, 91B', 'Milano'),
  (2, 'Pizza e fichi', 'Corso Italia, 21', 'Roma'),
  (3, 'Asian sushi bar', 'Via Shangai, 45', 'Firenze');

INSERT INTO REVIEW (ID, MESSAGE, STARS, RESTAURANT_ID) values
  (1, 'Il sushi sembra vivo', 4, 3),
  (2, 'Il sushi sembra vivo, che schifo', 1, 3),
  (3, 'la cameriera è bona', 5, 3),
  (4, 'il sushi è congelato', 2, 3);
