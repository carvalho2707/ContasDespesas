INSERT INTO Category (Name, Descricao) VALUES
('Alimentacao','Descricao'),
('Contas','Descricao'),
('Escola','Descricao'),
('Transportes','Descricao'),
('Habitacao','Descricao');

INSERT INTO SubCategory (Name,Descricao,CategoryID) VALUES
('Almoço','Descricao',1),
('Bolos','Descricao',1),
('Cafes','Descricao',1),
('Agua','Descricao',2),
('Internet','Descricao',2),
('Luz','Descricao',2),
('Autocarro','Descricao',4),
('Comboio','Descricao',4),
('Metro','Descricao',4),
('Propinas','Descricao',3),
('Livros','Descricao',3),
('Escritorio','Descricao',3),
('Portagem','Descricao',4),
('Gasolina','Descricao',4),
('Gasoleo','Descricao',4),
('Multas','Descricao',4),
('Oficina','Descricao',4),
('Prestacao carro','Descricao',4),
('Jantar fora','Descricao',1),
('Mercearia','Descricao',1),
('Talho','Descricao',1),
('Pao','Descricao',1),
('Piscina','Descricao',5),
('Jardim','Descricao',5),
('Lavandaria','Descricao',5),
('Seguro Recheio','Descricao',5),
('Seguro Multiriscos','Descricao',5),
('Reparacoes','Descricao',5),
('Prestacao','Descricao',5),
('Decoracao','Descricao',5),
('Seguro Vida Casa','Descricao',5);

INSERT INTO Person ( Name, Surname) VALUES
('Filipe','Carvalho'),
('João','Carvalho'),
('Tiago','Carvalho');

INSERT INTO Purchase (ItemName, DateOfPurchase, PersonID,CategoryID,Price,SubCategoryID) VALUES
('Propinas Tiago','2014-01-01',3,3,1000.2,10),
('Propinas Filpe','2014-01-02',1,3,1000.2,10),
('Propinas Jota','2014-01-02',2,3,1000.2,10),
('Propinas Tiago','2013-01-01',3,3,1000.2,10),
('Propinas Filpe','2013-01-02',1,3,1000.2,10),
('Propinas Jota','2013-01-02',2,3,1000.2,10),
('Propinas Tiago','2012-01-01',3,3,1000.2,10),
('Propinas Filpe','2012-01-02',1,3,1000.2,10),
('Propinas Jota','2012-01-02',2,3,1000.2,10),
('Mac','2014-01-01',3,1,5.2,1),
('Mac','2014-01-02',1,1,5.2,1),
('Mac','2014-01-02',2,1,5.2,1),
('Mac','2014-01-03',3,1,5.2,1),
('Mac','2014-01-04',1,1,5.2,1),
('Mac','2014-01-05',2,1,5.2,1),
('Mac','2014-01-06',3,1,5.2,1),
('Mac','2014-01-07',1,1,5.2,1),
('Mac','2014-01-08',2,1,5.2,1),
('Anos Pai','2014-01-06',3,1,50.2,1),
('Anos Jota','2014-01-07',1,1,49.2,1),
('Anos Filipe','2014-01-08',2,1,30.2,1),
('Anos Pai','2013-01-06',3,1,50.2,1),
('Anos Jota','2013-01-07',1,1,49.2,1),
('Anos Filipe','2013-01-08',2,1,30.2,1),
('Anos Pai','2012-01-06',3,1,50.2,1),
('Anos Jota','2012-01-07',1,1,49.2,1),
('Anos Filipe','2012-01-08',2,1,30.2,1),
('Anos Pai','2011-01-06',3,1,50.2,1),
('Anos Jota','2011-01-07',1,1,49.2,1),
('Anos Filipe','2011-01-08',2,1,30.2,1),
('Anos Pai','2010-01-06',3,1,50.2,1),
('Anos Jota','2010-01-07',1,1,49.2,1),
('Anos Filipe','2010-01-08',2,1,30.2,1),
('Mac','2013-01-01',3,1,5.2,1),
('Mac','2013-01-02',1,1,5.2,1),
('Mac','2013-01-02',2,1,5.2,1),
('Mac','2013-01-03',3,1,5.2,1),
('Mac','2013-01-04',1,1,5.2,1),
('Mac','2013-01-05',2,1,5.2,1),
('Mac','2013-01-06',3,1,5.2,1),
('Mac','2013-01-07',1,1,5.2,1),
('Mac','2013-01-08',2,1,5.2,1),
('Mac','2012-01-05',2,1,5.2,1),
('Mac','2012-01-06',3,1,5.2,1),
('Mac','2012-01-07',1,1,5.2,1),
('Mac','2012-01-08',2,1,5.2,1),
('Anos Pai','2009-01-06',3,1,50.2,1),
('Anos Jota','2009-01-07',1,1,49.2,1),
('Anos Filipe','2009-01-08',2,1,30.2,1),
('Cafe','2014-01-06',3,3,0.35,3),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Lanche Manha','2012-01-08',3,1,2.1,2),
('Introducao ao C','2014-01-06',3,3,20.4,10),
('Cadernos','2014-01-07',2,3,14.2,11),
('Canetas','2014-01-08',3,3,2.1,11),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Novabase','2014-01-06',3,4,1.80,8),
('Novabase','2014-01-08',3,4,1.80,8),
('Novabase','2014-01-09',3,4,1.80,8),
('Autocarro','2014-01-06',3,4,3.2,7),
('Autocarro','2014-01-05',3,4,3.1,7),
('Novabase','2013-01-06',3,4,1.80,8),
('Novabase','2013-01-08',3,4,1.80,8),
('Novabase','2013-01-09',3,4,1.80,8),
('Autocarro','2013-01-06',3,4,3.2,7),
('Propinas Tiago','2014-01-01',3,3,1000.2,10),
('Propinas Filpe','2014-01-02',1,3,1000.2,10),
('Propinas Jota','2014-01-02',2,3,1000.2,10),
('Propinas Tiago','2013-01-01',3,3,1000.2,10),
('Propinas Filpe','2013-01-02',1,3,1000.2,10),
('Propinas Jota','2013-01-02',2,3,1000.2,10),
('Propinas Tiago','2012-01-01',3,3,1000.2,10),
('Propinas Filpe','2012-01-02',1,3,1000.2,10),
('Propinas Jota','2012-01-02',2,3,1000.2,10),
('Mac','2014-01-01',3,1,5.2,1),
('Mac','2014-01-02',1,1,5.2,1),
('Mac','2014-01-02',2,1,5.2,1),
('Mac','2014-01-03',3,1,5.2,1),
('Mac','2014-01-04',1,1,5.2,1),
('Mac','2014-01-05',2,1,5.2,1),
('Mac','2014-01-06',3,1,5.2,1),
('Mac','2014-01-07',1,1,5.2,1),
('Mac','2014-01-08',2,1,5.2,1),
('Anos Pai','2014-01-06',3,1,50.2,1),
('Anos Jota','2014-01-07',1,1,49.2,1),
('Anos Filipe','2014-01-08',2,1,30.2,1),
('Anos Pai','2013-01-06',3,1,50.2,1),
('Anos Jota','2013-01-07',1,1,49.2,1),
('Anos Filipe','2013-01-08',2,1,30.2,1),
('Anos Pai','2012-01-06',3,1,50.2,1),
('Anos Jota','2012-01-07',1,1,49.2,1),
('Anos Filipe','2012-01-08',2,1,30.2,1),
('Anos Pai','2011-01-06',3,1,50.2,1),
('Anos Jota','2011-01-07',1,1,49.2,1),
('Anos Filipe','2011-01-08',2,1,30.2,1),
('Anos Pai','2010-01-06',3,1,50.2,1),
('Anos Jota','2010-01-07',1,1,49.2,1),
('Anos Filipe','2010-01-08',2,1,30.2,1),
('Mac','2013-01-01',3,1,5.2,1),
('Mac','2013-01-02',1,1,5.2,1),
('Mac','2013-01-02',2,1,5.2,1),
('Mac','2013-01-03',3,1,5.2,1),
('Mac','2013-01-04',1,1,5.2,1),
('Mac','2013-01-05',2,1,5.2,1),
('Mac','2013-01-06',3,1,5.2,1),
('Mac','2013-01-07',1,1,5.2,1),
('Mac','2013-01-08',2,1,5.2,1),
('Mac','2012-01-05',2,1,5.2,1),
('Mac','2012-01-06',3,1,5.2,1),
('Mac','2012-01-07',1,1,5.2,1),
('Mac','2012-01-08',2,1,5.2,1),
('Anos Pai','2009-01-06',3,1,50.2,1),
('Anos Jota','2009-01-07',1,1,49.2,1),
('Anos Filipe','2009-01-08',2,1,30.2,1),
('Cafe','2014-01-06',3,3,0.35,3),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Lanche Manha','2012-01-08',3,1,2.1,2),
('Introducao ao C','2014-01-06',3,3,20.4,10),
('Cadernos','2014-01-07',2,3,14.2,11),
('Canetas','2014-01-08',3,3,2.1,11),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Novabase','2014-01-06',3,4,1.80,8),
('Novabase','2014-01-08',3,4,1.80,8),
('Novabase','2014-01-09',3,4,1.80,8),
('Autocarro','2014-01-06',3,4,3.2,7),
('Autocarro','2014-01-05',3,4,3.1,7),
('Novabase','2013-01-06',3,4,1.80,8),
('Novabase','2013-01-08',3,4,1.80,8),
('Novabase','2013-01-09',3,4,1.80,8),
('Autocarro','2013-01-06',3,4,3.2,7),
('Propinas Tiago','2014-01-01',3,3,1000.2,10),
('Propinas Filpe','2014-01-02',1,3,1000.2,10),
('Propinas Jota','2014-01-02',2,3,1000.2,10),
('Propinas Tiago','2013-01-01',3,3,1000.2,10),
('Propinas Filpe','2013-01-02',1,3,1000.2,10),
('Propinas Jota','2013-01-02',2,3,1000.2,10),
('Propinas Tiago','2012-01-01',3,3,1000.2,10),
('Propinas Filpe','2012-01-02',1,3,1000.2,10),
('Propinas Jota','2012-01-02',2,3,1000.2,10),
('Mac','2014-01-01',3,1,5.2,1),
('Mac','2014-01-02',1,1,5.2,1),
('Mac','2014-01-02',2,1,5.2,1),
('Mac','2014-01-03',3,1,5.2,1),
('Mac','2014-01-04',1,1,5.2,1),
('Mac','2014-01-05',2,1,5.2,1),
('Mac','2014-01-06',3,1,5.2,1),
('Mac','2014-01-07',1,1,5.2,1),
('Mac','2014-01-08',2,1,5.2,1),
('Anos Pai','2014-01-06',3,1,50.2,1),
('Anos Jota','2014-01-07',1,1,49.2,1),
('Anos Filipe','2014-01-08',2,1,30.2,1),
('Anos Pai','2013-01-06',3,1,50.2,1),
('Anos Jota','2013-01-07',1,1,49.2,1),
('Anos Filipe','2013-01-08',2,1,30.2,1),
('Anos Pai','2012-01-06',3,1,50.2,1),
('Anos Jota','2012-01-07',1,1,49.2,1),
('Anos Filipe','2012-01-08',2,1,30.2,1),
('Anos Pai','2011-01-06',3,1,50.2,1),
('Anos Jota','2011-01-07',1,1,49.2,1),
('Anos Filipe','2011-01-08',2,1,30.2,1),
('Anos Pai','2010-01-06',3,1,50.2,1),
('Anos Jota','2010-01-07',1,1,49.2,1),
('Anos Filipe','2010-01-08',2,1,30.2,1),
('Mac','2013-01-01',3,1,5.2,1),
('Mac','2013-01-02',1,1,5.2,1),
('Mac','2013-01-02',2,1,5.2,1),
('Mac','2013-01-03',3,1,5.2,1),
('Mac','2013-01-04',1,1,5.2,1),
('Mac','2013-01-05',2,1,5.2,1),
('Mac','2013-01-06',3,1,5.2,1),
('Mac','2013-01-07',1,1,5.2,1),
('Mac','2013-01-08',2,1,5.2,1),
('Mac','2012-01-05',2,1,5.2,1),
('Mac','2012-01-06',3,1,5.2,1),
('Mac','2012-01-07',1,1,5.2,1),
('Mac','2012-01-08',2,1,5.2,1),
('Anos Pai','2009-01-06',3,1,50.2,1),
('Anos Jota','2009-01-07',1,1,49.2,1),
('Anos Filipe','2009-01-08',2,1,30.2,1),
('Cafe','2014-01-06',3,3,0.35,3),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Cafe','2014-01-07',3,3,0.35,3),
('Cafe','2014-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Cafe','2013-01-06',3,3,0.35,3),
('Cafe','2013-01-07',3,3,0.35,3),
('Cafe','2013-01-08',3,3,0.35,3),
('Lanche Manha','2014-01-06',3,1,2.4,2),
('Lanche Tarde','2014-01-07',2,1,5.2,2),
('Lanche Manha','2014-01-08',3,1,2.1,2),
('Lanche Manha','2013-01-06',1,1,2.4,2),
('Lanche Tarde','2013-01-07',2,1,5.2,2),
('Lanche Manha','2013-01-08',3,1,2.1,2),
('Lanche Manha','2012-01-06',1,1,2.4,2),
('Lanche Tarde','2012-01-07',2,1,5.2,2),
('Lanche Manha','2012-01-08',3,1,2.1,2),
('Introducao ao C','2014-01-06',3,3,20.4,10),
('Cadernos','2014-01-07',2,3,14.2,11),
('Canetas','2014-01-08',3,3,2.1,11),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Metro','2014-01-06',3,4,11.1,9),
('Novabase','2014-01-06',3,4,1.80,8),
('Novabase','2014-01-08',3,4,1.80,8),
('Novabase','2014-01-09',3,4,1.80,8),
('Autocarro','2014-01-06',3,4,3.2,7),
('Autocarro','2014-01-05',3,4,3.1,7),
('Novabase','2013-01-06',3,4,1.80,8),
('Novabase','2013-01-08',3,4,1.80,8),
('Novabase','2013-01-09',3,4,1.80,8),
('Autocarro','2013-01-06',3,4,3.2,7),
('Autocarro','2013-01-05',3,4,3.1,7);


