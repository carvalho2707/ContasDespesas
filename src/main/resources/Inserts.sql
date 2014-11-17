MYSQL

INSERT INTO Category VALUES
(1,'Categoria numero 1','Descricao numero 1',0.0),
(2,'Categoria numero 2','Descricao numero 2',0.0),
(3,'Categoria numero 3','Descricao numero 3',0.0),
(4,'Categoria numero 4','Descricao numero 4',0.0),
(5,'Categoria numero 5','Descricao numero 5',0.0),
(6,'Categoria numero 6','Descricao numero 6',0.0),
(7,'Categoria numero 7','Descricao numero 7',0.0);

INSERT INTO Person VALUES
(1,'Nome pessoa numero 1','Apelido pessoa numero 1'),
(2,'Nome pessoa numero 2','Apelido pessoa numero 2'),
(3,'Nome pessoa numero 3','Apelido pessoa numero 3'),
(4,'Nome pessoa numero 4','Apelido pessoa numero 4'),
(5,'Nome pessoa numero 5','Apelido pessoa numero 5'),
(6,'Nome pessoa numero 6','Apelido pessoa numero 6'),
(7,'Nome pessoa numero 7','Apelido pessoa numero 7');


JAVA DB

INSERT INTO Category (Name, Descricao,Total) VALUES 
('Categoria numero 1','Descricao numero 1',0.0),
('Categoria numero 2','Descricao numero 2',0.0),
('Categoria numero 3','Descricao numero 3',0.0),
('Categoria numero 4','Descricao numero 4',0.0),
('Categoria numero 5','Descricao numero 5',0.0),
('Categoria numero 6','Descricao numero 6',0.0),
('Categoria numero 7','Descricao numero 7',0.0);

INSERT INTO Person (Name, Surname) VALUES
('Nome pessoa numero 1','Apelido pessoa numero 1'),
('Nome pessoa numero 2','Apelido pessoa numero 2'),
('Nome pessoa numero 3','Apelido pessoa numero 3'),
('Nome pessoa numero 4','Apelido pessoa numero 4'),
('Nome pessoa numero 5','Apelido pessoa numero 5'),
('Nome pessoa numero 6','Apelido pessoa numero 6'),
('Nome pessoa numero 7','Apelido pessoa numero 7');


INSERT INTO Purchase (ItemName, DateOfPurchase, PersonID,CategoryID,Price) VALUES
('Nome item numero 1','2014-01-01',1,1,11.1),
('Nome item numero 2','2014-02-02',1,1,22.2),
('Nome item numero 3','2014-03-03',1,1,33.3),
('Nome item numero 4','2014-04-04',2,1,44.4),
('Nome item numero 5','2014-05-05',2,1,55.5),
('Nome item numero 6','2014-06-06',3,1,66.6),
('Nome item numero 7','2014-07-07',4,1,77.7),
('Nome item numero 8','2014-12-29',2,1,150.7),
('Nome item numero 9','2014-01-01',3,1,150.7),
('Nome item numero 10','2014-01-01',1,1,150.7),
('Nome item numero 11','2014-01-01',1,1,150.7),
('Nome item numero 12','2014-01-01',1,1,80.7);



INSERT INTO Purchase (ItemName, DateOfPurchase, PersonID,CategoryID,Price) VALUES
('Nome item numero 1',STR_TO_DATE('01-01-2014','%d-%m-%y'),1,1,11.1),
('Nome item numero 2',STR_TO_DATE('02-02-2014','%d-%m-%y'),1,1,22.2),
('Nome item numero 3',STR_TO_DATE('03-03-2014','%d-%m-%y'),1,1,33.3),
('Nome item numero 4',STR_TO_DATE('04-04-2014','%d-%m-%y'),2,1,44.4),
('Nome item numero 5',STR_TO_DATE('05-05-2014','%d-%m-%y'),2,1,55.5),
('Nome item numero 6',STR_TO_DATE('06-06-2014','%d-%m-%y'),3,1,66.6),
('Nome item numero 7',STR_TO_DATE('25-01-2014','%d-%m-%y'),4,1,77.7),
('Nome item numero 8',STR_TO_DATE('14-01-2014','%d-%m-%y'),1,1,150.7),
('Nome item numero 9',STR_TO_DATE('22-02-2014','%d-%m-%y'),1,1,80.7);

SELECT MIN(YEAR(DateOfPurchase)) AS inicial FROM Purchase;






















