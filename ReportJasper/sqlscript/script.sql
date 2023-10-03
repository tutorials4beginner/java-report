CREATE DATABASE myjasper;

USE myjasper;

CREATE TABLE `studentdetails` (
	`Name` varchar(20) NOT NULL,
	`Age` int(3) NOT NULL,
	`Country` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO myjasper.studentdetails 
	(Name, Age, Country)
	VALUES ('Name', Age, 'Country');  