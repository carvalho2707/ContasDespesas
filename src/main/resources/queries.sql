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
Price FLOAT(9,3) NOT NULL,
SubCategoryID INT NOT NULL,
FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (CategoryID) REFERENCES Category(ID)ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (SubCategoryID) REFERENCES SubCategory(ID)ON DELETE CASCADE
       ON UPDATE CASCADE);