CREATE database if NOT EXISTS timeClockDB;

use timeClockDB;

DROP TABLE if EXISTS employees;

CREATE TABLE employees (
  EmployeeID INT NOT NULL UNIQUE AUTO_INCREMENT,
  FirstName VARCHAR(64),
  LastName VARCHAR(64),
  Email VARCHAR(64),
  Password VARCHAR(64),
  EmployeeType VARCHAR(64),
  PRIMARY KEY(EmployeeID)
);

CREATE TABLE clock (
  EmployeeID int NOT NULL,
  PunchTime dateTime,
  PunchType bit);
 go