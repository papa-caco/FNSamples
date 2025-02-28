select * from FnAccessPermission;
select * from FnOwner;
select * from Principal;
select * from SecurableObject;

BULK INSERT dbo.FnAccessPermission
FROM 'C:\Users\Carlos\git-repos\FNSamples\info\FnAccessPermission.csv' WITH
(FIRSTROW = 2, 
 ROWTERMINATOR='\n', 
 FIELDTERMINATOR = ';' , 
 BATCHSIZE=25000,
 MAXERRORS = 100,
 CODEPAGE = 'ACP',
 ERRORFILE = 'C:\Users\Carlos\git-repos\FNSamples\info\FnAccessPermission.log'); 

 BULK INSERT dbo.FnOwner
FROM 'C:\Users\Carlos\git-repos\FNSamples\info\FnOwner.csv' WITH
(FIRSTROW = 2, 
 ROWTERMINATOR='\n', 
 FIELDTERMINATOR = ';' , 
 BATCHSIZE=25000,
 MAXERRORS = 100,
 CODEPAGE = 'ACP',
 ERRORFILE = 'C:\Users\Carlos\git-repos\FNSamples\info\FnOwner.log'); 

 BULK INSERT dbo.Principal
FROM 'C:\Users\Carlos\git-repos\FNSamples\info\Principal.csv' WITH
(FIRSTROW = 2, 
 ROWTERMINATOR='\n', 
 FIELDTERMINATOR = ';' , 
 BATCHSIZE=25000,
 MAXERRORS = 100,
 CODEPAGE = 'ACP',
 ERRORFILE = 'C:\Users\Carlos\git-repos\FNSamples\info\Principal.log'); 

 delete from Principal;
 delete from FnOwner;
 delete from FnAccessPermission;


     create table SecurableObject (
       idSecurableObject int not null,
        fnObjectType varchar(255),
        processStatus char(1),
        rowCount int,
        securityIdCount int,
        tableName varchar(255),
        primary key (idSecurableObject)