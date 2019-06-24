CREATE DATABASE timeClockDB;
go
USE timeClockDB;
go

CREATE TABLE employees (
  EmployeeID INT NOT NULL UNIQUE AUTO_INCREMENT,
  FirstName VARCHAR(255),
  LastName VARCHAR(255),
  EmployeeType VARCHAR(255),
  PRIMARY KEY(EmployeeID)
);
go

CREATE TABLE clock (
  EmployeeID int NOT NULL,
  PunchTime dateTime,
  PunchType bit);
 go