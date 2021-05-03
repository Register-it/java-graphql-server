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
  RATING INT(1) NOT NULL,
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
    (3, 'Asian sushi bar', 'Via Shangai, 45', 'Firenze'),
    (4,'Ciccio Marina', 'Viale Giovanni da Verazzano 1', 'Firenze'),
    (5,'Enoteca Velia', 'Via A. Manzoni, 1', 'Firenze'),
    (6,'Il Buscaiol', 'Via Genova 6', 'Firenze'),
    (7,'IL REBACCO', 'Via Loris Giorgi, 5', 'Bologna'),
    (8,'La Capinera Osteria tipica', 'Via Ulivi 8', 'Bologna'),
    (9,'La Petite Cuisine', 'Via Verdi 4', 'Bologna'),
    (10,'Le Petite Cuisine', 'Via Verdi 4', 'Bologna'),
    (11,'l Purtunzin Ninan', 'Via bartolini 3', 'Milano'),
    (12,'OSTERIA MEROPE', 'Via ULIVI 4', 'Milano'),
    (13,'Osteria Vittorio', 'Via Cavour 28', 'Milano'),
    (14,'Ristorante Club Nautico', 'Viale Cristoforo, 2', 'Milano'),
    (15,'RISTORANTE LA TAVERNETTA', 'Piazza Alberica 10', 'Roma'),
    (16,'Venanzio', 'Piazza Palestro 3', 'Roma'),
    (17,'La Bottega dei Parchi', 'Piazza dei Parchi', 'Roma'),
    (18,'Albergo Ristorante Il Giardinetto', 'Via Roma 155', 'Roma'),
    (19,'Da Remo', 'Via Cesare Battisti 57', 'Roma'),
    (20,'Il Sicomoro', 'Via pianezza 2', 'Torino'),
    (21,'La Posta', 'Via Provinciale 15', 'Torino'),
    (22,'Nigo Pezigo', 'Via Papiriana', 'Torino'),
    (23,'Nigo Pezigo', 'Via Papiriana 1A', 'Genova'),
    (24,'RISTORANTE AL FALCO', 'Via PIANA DI COPINO 1FOSDINOVO', 'Genova'),
    (25,'Ristorante Cocopizza', 'Via Caniparola 7', 'Genova');

INSERT INTO REVIEW (ID, MESSAGE, RATING, RESTAURANT_ID) values
  (1, 'Il sushi sembra vivo', 4, 3),
  (2, 'Il sushi sembra vivo, che schifo', 1, 3),
  (3, 'la cameriera è bona', 5, 3),
  (4, 'Lo chef vorrebbe creare una cucina post-molecolare e nostalgica, che sussurra, invece, l’uso dissennato della cottura sous vide, con risultati oscuramente mistici.', 2, 16),
  (5, 'Il servizio è accurato con tocchi casuali di scortesia.', 4, 8),
  (6, 'L’ambiente è del genere metropolitano–trasandato con inutili tovaglie e tovaglioli in resina disegnati dal noto studio architettonico che ha firmato tutti i locali del gruppo.', 1, 16),
  (7, 'Nel complesso, il locale dovrebbe cercare di cambiare colore alle tovaglie, se desidera costruirsi una nomea più altisonante.', 2, 3),
  (8, 'Lo chef vorrebbe creare una cucina ingegnosa e domestica, che tradisce, invece, la sua preparazione decennale, con risultati decisamente disruptive.', 4, 14),
  (9, 'Il servizio è complice con pizzichi di cabaret.', 2, 1),
  (10, 'L’ambiente è del genere stiloso–raffinato con inutili tavoli hi-tech disegnati dal noto studio architettonico che ha studiato il restyling del MOMA.', 2, 4),
  (11, 'Nel complesso, il locale dovrebbe cercare di salutare meno calorosamente gli amici, se desidera costruirsi un’allure più chic & cheap.', 1, 16),
  (12, 'Lo chef si illude di creare una cucina spigolosa e tradizionale, che tradisce, invece, la sua ossessione per il cubismo, con risultati , volendo essere generosi, liquidi, in senso baumaniano.', 4, 3),
  (13, 'Il servizio è frugale con nappature di indiscrezione.', 1, 19),
  (14, 'L’ambiente è del genere casual–trascurato con bizzarri gradini in poliuretano smaltato disegnati dal noto studio architettonico che ha disegnato gli interni dell’ultimo film di Sorrentino.', 2, 2),
  (15, 'Nel complesso, il locale dovrebbe cercare di cambiare colore alle tovaglie, se desidera costruirsi un’allure più cosmopolita.', 1, 22),
  (16, 'Lo chef vorrebbe creare una cucina ingegnosa e con un’anima antica, che grida al mondo, invece, la sua ossessione per il panko, con risultati timidamente lisergici.', 4, 5),
  (17, 'Il servizio è frugale con pizzichi di teatro dell’arte.', 1, 9),
  (18, 'L’ambiente è del genere stiloso–inelegante con curiosi finestroni avveniristici disegnati dal noto studio architettonico che ha inventato il temporary shop come lo conosciamo.', 3, 15),
  (19, 'Nel complesso, il locale dovrebbe cercare di far ripensare il sottofondo musicale ad un sound designer, se desidera costruirsi uno stile più up-to-date.', 1, 21),
  (20, 'Lo chef vorrebbe creare una cucina universale e nostalgica, che disegna, invece, la sua ossessione per il panko, con risultati tendenzialmente visionari.', 3, 13),
  (21, 'Il servizio è minimale con nappature di lentezza.', 4, 22),
  (22, 'L’ambiente è del genere elegante–trascurato con notevoli piatti in poliuretano smaltato disegnati dal noto studio architettonico che ha firmato tutti i locali del gruppo.', 4, 9),
  (23, 'Nel complesso, il locale dovrebbe cercare di mettere più fiori freschi in tavola, se desidera costruirsi un riconoscimento più soprendente.', 2, 21),
  (24, 'Lo chef ricrea una cucina spigolosa e domestica, che rivela, invece, la sua ossessione per l’umami, con risultati abbastanza visionari.', 2, 9),
  (25, 'Il servizio è premuroso con tocchi casuali di improvvisazione.', 3, 23),
  (26, 'L’ambiente è del genere casereccio–trascurato con notevoli finestroni hi-tech disegnati dal noto studio architettonico specializzato in archeologia urbana.', 4, 23),
  (27, 'Nel complesso, il locale dovrebbe cercare di far disegnare le divise ad uno stilista di pregio, se desidera costruirsi una clientela più rispondente alle sue premesse.', 4, 3);
