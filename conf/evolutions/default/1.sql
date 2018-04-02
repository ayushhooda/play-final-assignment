# --- !Ups

CREATE TABLE user_details (
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    fname varchar(30) NOT NULL,
    mname varchar(30) NOT NULL,
    lname varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    password varchar(50) NOT NULL,
    mobile char(10) NOT NULL,
    gender varchar(10) NOT NULL,
    age int(11) NOT NULL,
    hobbies varchar(50),
    isEnabled tinyint(1),
    isAdmin tinyint(1)
);

CREATE TABLE assignment_details (
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    title varchar(50) NOT NULL,
    description varchar(500) NOT NULL
);

# --- !Downs
DROP TABLE user_details;
DROP TABLE assignment_details;
