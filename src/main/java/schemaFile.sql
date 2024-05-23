DROP TABLE IF EXISTS university.COURSES CASCADE;
DROP TABLE IF EXISTS university.GROUPS;
DROP TABLE IF EXISTS university.STUDENTS CASCADE;
DROP TABLE IF EXISTS university.students_courses;


CREATE TABLE IF NOT EXISTS university.STUDENTS(id SERIAL PRIMARY KEY not null, group_id integer, first_name character(255), last_name character(255));

CREATE TABLE IF NOT EXISTS university.COURSES(id SERIAL PRIMARY KEY not null, course_name character(50), course_description character(255));

CREATE TABLE IF NOT EXISTS university.GROUPS (group_id SERIAL PRIMARY KEY not null, group_name character(255));

CREATE TABLE IF NOT EXISTS university.students_courses (student_id INTEGER NOT NULL, course_id INTEGER NOT NULL, PRIMARY KEY(student_id, course_id), FOREIGN KEY (student_id) REFERENCES university.STUDENTS(id), FOREIGN KEY (course_id) REFERENCES university.courses(id));
