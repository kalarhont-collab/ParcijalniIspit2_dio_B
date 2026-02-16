CREATE DATABASE JavaAdvHibernate
GO

USE JavaAdvHibernate
GO


-- TABLICE --

CREATE TABLE Polaznik (
PolaznikID INT PRIMARY KEY IDENTITY,
Ime nvarchar(100) NOT NULL,
Prezime nvarchar(100) NOT NULL
);

CREATE TABLE ProgramObrazovanja (
ProgramObrazovanjaID INT PRIMARY KEY IDENTITY,
Naziv nvarchar(100) NOT NULL,
CSVET INT NOT NULL
);

CREATE TABLE Upis (
UpisID INT PRIMARY KEY IDENTITY,
IDPolaznik INT CONSTRAINT FK_Upis_Polaznik
FOREIGN KEY (IDPolaznik) REFERENCES Polaznik(PolaznikID) NOT NULL,
IDProgramObrazovanja INT CONSTRAINT FK_Upis_ProgramObrazovanja
FOREIGN KEY (IDProgramObrazovanja) REFERENCES ProgramObrazovanja(ProgramObrazovanjaID) NOT NULL
);
