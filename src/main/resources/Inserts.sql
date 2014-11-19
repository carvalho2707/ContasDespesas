INSERT INTO Category (Name, Descricao)) VALUES
('Categoria  1','Descricao  1'),
('Categoria  2','Descricao  2'),
('Categoria  3','Descricao  3'),
('Categoria  4','Descricao  4');

INSERT INTO SubCategory (Name,Descricao,CategoryID) VALUES
('Sub  1','Descricao  1',1),
('Sub  2','Descricao  2',1),
('Sub  3','Descricao  3',1);

INSERT INTO Person ( Name, Surname) VALUES
('Nome 1','Apelido 1'),
('Nome 2','Apelido 2'),
('Nome 3','Apelido 3'),
('Nome 4','Apelido 4');

INSERT INTO Purchase (ItemName, DateOfPurchase, PersonID,CategoryID,Price,SubCategoryID) VALUES
('Nome item numero 1','2014-01-01',1,1,11.1,1),
('Nome item numero 2','2014-02-02',1,1,22.2,2),
('Nome item numero 3','2014-03-03',1,1,33.3,2);