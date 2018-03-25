CREATE DATABASE `ankit` /*!40100 DEFAULT CHARACTER SET utf8 */;

use ankit;

CREATE TABLE `course` (
  `crsCode` varchar(45) NOT NULL,
  `deptId` varchar(45) DEFAULT NULL,
  `crsName` varchar(45) DEFAULT NULL,
  `descr` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`crsCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `professor` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `deptId` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `transcript` (
  `studId` int(11) NOT NULL,
  `crsCode` varchar(45) NOT NULL,
  `semester` varchar(45) NOT NULL,
  `grade` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`studId`,`crsCode`,`semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `teaching` (
  `crsCode` varchar(45) NOT NULL,
  `semester` varchar(45) NOT NULL,
  `profId` int(11) DEFAULT NULL,
  PRIMARY KEY (`crsCode`,`semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



SET SQL_SAFE_UPDATES = 0;
Delete from ankit.student;
Delete from ankit.course;

Delete from ankit.professor;
Delete from ankit.teaching;
Delete from ankit.transcript;
