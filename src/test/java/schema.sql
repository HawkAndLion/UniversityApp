DROP TABLE IF EXISTS university.students_courses cascade; 
DROP TABLE IF EXISTS university.STUDENTS cascade;
DROP TABLE IF EXISTS university.courses cascade;
DROP TABLE IF EXISTS university.GROUPS cascade;

CREATE SCHEMA IF NOT EXISTS university;
SET SCHEMA university;

CREATE TABLE IF NOT EXISTS university.STUDENTS(id int PRIMARY KEY AUTO_INCREMENT not null, group_id integer, first_name varchar(50), last_name varchar(50));

INSERT INTO university.STUDENTS (id, group_id, first_name, last_name) VALUES (105, 201, 'Charles', 'Xavier');

CREATE TABLE IF NOT EXISTS university.COURSES(id int AUTO_INCREMENT not null, course_name varchar(50), course_description varchar(255), PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS university.students_courses (student_id INTEGER NOT NULL, course_id INTEGER NOT NULL, PRIMARY KEY(student_id, course_id), FOREIGN KEY (student_id) REFERENCES STUDENTS(id), FOREIGN KEY (course_id) REFERENCES courses(id));


INSERT INTO university.courses (course_name, course_description) VALUES ('Algebra', 'Develops algebraic concepts and skills.');
INSERT INTO university.courses (course_name, course_description) VALUES ( 'Geometry', 'Presents the study of several geometries');
INSERT INTO university.courses (course_name, course_description) VALUES ( 'Geography', 'An introduction to the earth’s natural environmental systems.');

CREATE TABLE IF NOT EXISTS university.GROUPS (group_id int AUTO_INCREMENT not null, group_name varchar(50));

INSERT INTO university.GROUPS (group_id, group_name) VALUES (7, 'NL-77');