create user 'raul'@'localhost' identified by '1234';
grant all privileges on * to 'raul'@'localhost';
flush privileges;
exit;

mysql -u raul -p
1234

create database datasource_warehouse;
use datasource_warehouse;
CREATE TABLE Warehouses (
   Code INTEGER NOT NULL,
   Location VARCHAR(255) NOT NULL ,
   PRIMARY KEY (Code)
 );
CREATE TABLE Boxes (
    Code VARCHAR(255) NOT NULL,
    Contents VARCHAR(255) NOT NULL ,
    Value REAL NOT NULL ,
    Warehouse INTEGER NOT NULL,
    PRIMARY KEY (Code),
    FOREIGN KEY (Warehouse) REFERENCES Warehouses(Code)
 ) ENGINE=INNODB;
 
 INSERT INTO Warehouses(Code,Location) VALUES(1,'Chicago');
 INSERT INTO Warehouses(Code,Location) VALUES(2,'Chicago');
 INSERT INTO Warehouses(Code,Location) VALUES(3,'New York');
 INSERT INTO Warehouses(Code,Location) VALUES(4,'Los Angeles');
 INSERT INTO Warehouses(Code,Location) VALUES(5,'San Francisco');
 
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('0MN7','Rocks',180,3);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('4H8P','Rocks',250,1);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('4RT3','Scissors',190,4);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('7G3H','Rocks',200,1);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('8JN6','Papers',75,1);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('8Y6U','Papers',50,3);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('9J6F','Papers',175,2);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('LL08','Rocks',140,4);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('P0H6','Scissors',125,1);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('P2T6','Scissors',150,2);
 INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('TU55','Papers',90,5);
 
 ____________________________________________________________________________________________________
 
 Se han cogido algunos fragmentos de:
 https://github.com/McD0n3ld/ServletConcierto.git
 
 URL de la app:
 http://localhost:8080/examen/servlet
 
 Consideraciones:
 1) Se puede sacar una caja y se añade a la lista de cambios.
 2) Se puede crear una caja y se añade a la lista de cambios.
 3) Una caja que hayas sacado, se le puede asignar un nuevo almacen desde la lista de cambios.
 4) Se pueden eliminar acciones sobre la caja
 5) Se pueden validar cambios, haciendo un INSERT si son caja nuevas, un UPDATE si son cajas que han cambiado de almacen o DELETE si son cajas que no pertenecen a ningun almacen.