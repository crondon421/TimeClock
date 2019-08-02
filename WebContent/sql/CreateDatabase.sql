CREATE database if NOT EXISTS timeClockDB;

use timeclockdb;

DROP TABLE if EXISTS employee;

CREATE TABLE employee (
  EmployeeID INT NOT NULL UNIQUE AUTO_INCREMENT,
  FirstName VARCHAR(64),
  LastName VARCHAR(64),
  Email VARCHAR(64),
  Password VARCHAR(64),
  EmployeeType VARCHAR(64),
  PRIMARY KEY(EmployeeID)
) ENGINE=InnoDB;

INSERT INTO timeclockdb.employee (FirstName, LastName, Email, Password, EmployeeType)
VALUES('John', 'Smith', 'John', 'Smith', 'service');

INSERT INTO timeclockdb.employee (FirstName, LastName, Email, Password, EmployeeType)
VALUES('Valerie', 'Johnson', 'Valerie', 'Johnson', 'admin');

DROP TABLE if EXISTS clock;
CREATE TABLE clock (
  PunchId INT NOT NULL UNIQUE AUTO_INCREMENT,
  EmployeeID INT NOT NULL,
  PunchTime DATETIME,
  PunchType BIT,
  PRIMARY KEY(PunchId)
)ENGINE=InnoDB;

INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
	VALUES (1, '2019-06-18 08:00:00', 1);
    
INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
	VALUES (1, '2019-06-18 17:00:00', 0);

INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
	VALUES (1, '2019-06-19 08:00:00', 1);
    
INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
	VALUES (1, '2019-06-19 17:00:00', 0);
    
INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
VALUES (1, '2019-07-02 00:00:00', 1);

INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
VALUES (1, '2019-07-02 09:46:00', 0);

INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
VALUES (1, '2019-07-08 00:00:00', 1);

INSERT INTO timeclockdb.clock (EmployeeID, PunchTime, PunchType)
VALUES (1, '2019-07-08 08:00:00', 0);
    
