DROP TABLE Purchase;
DROP TABLE SubCategory;
DROP TABLE Person;
DROP TABLE Category;

CREATE TABLE Category(
ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(250) NOT NULL,
Descricao VARCHAR(250));

CREATE TABLE SubCategory(
ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(250) NOT NULL,
Descricao VARCHAR(250),
CategoryID INT NOT NULL,
FOREIGN KEY (CategoryID) REFERENCES Category(ID)ON DELETE CASCADE
       ON UPDATE CASCADE);

CREATE TABLE Person(
ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(250) NOT NULL,
Surname VARCHAR(250) NOT NULL
);


CREATE TABLE Purchase(
ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
ItemName VARCHAR(250) NOT NULL,
DateOfPurchase DATE NOT NULL,
PersonID INT NOT NULL,
CategoryID INT NOT NULL,
Price DOUBLE NOT NULL,
SubCategoryID INT NOT NULL,
FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (CategoryID) REFERENCES Category(ID)ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (SubCategoryID) REFERENCES SubCategory(ID)ON DELETE CASCADE
       ON UPDATE CASCADE);
	   
	   
	   
INSERT INTO Category (Name, Descricao) VALUES
('Alimentacao','Descricao'),
('Contas','Descricao'),
('Escola','Descricao'),
('Transportes','Descricao');

INSERT INTO SubCategory (Name,Descricao,CategoryID) VALUES
('Almoço','Descricao',1),
('Bolos','Descricao',1),
('Cafes','Descricao',1),
('Agua','Descricao',2),
('Internet','Descricao',2),
('Luz','Descricao',2),
('Autocarro','Descricao',4),
('Comboio','Descricao',4),
('Metro','Descricao',4);

INSERT INTO Person ( Name, Surname) VALUES
('Filipe','Carvalho'),
('João','Carvalho'),
('Tiago','Carvalho');

INSERT INTO Purchase (ItemName, DateOfPurchase, PersonID,CategoryID,Price,SubCategoryID) VALUES
('Mac','2014-01-01',3,1,5.2,1),
('Mac','2014-01-02',1,1,5.2,1),
('Mac','2014-01-02',2,1,5.2,1),
('Mac','2014-01-03',3,1,5.2,1),
('Mac','2014-01-04',1,1,5.2,1),
('Mac','2014-01-05',2,1,5.2,1),
('Mac','2014-01-06',3,1,5.2,1),
('Mac','2014-01-07',1,1,5.2,1),
('Mac','2014-01-08',2,1,5.2,1),
('Cafe','2014-01-06',3,3,0.35,3),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9);
