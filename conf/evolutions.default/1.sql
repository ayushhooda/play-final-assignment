# --- !Ups

CREATE TABLE user_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fname varchar(30) NOT NULL,
    lname varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    password varchar(50) NOT NULL,

);

# --- !Downs
DROP TABLE user_details;