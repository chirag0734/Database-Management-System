
/* unoptimized Query*/

 -- Query1 : 1.	List the name of the student with id equal to v1 (id).
SELECT s.name FROM student s where s.id = '898275393';

-- Query 2: List the names of students with id in the range of v2 (id) to v3 (inclusive).

select s.name from student s where s.id between v2 and v3;

-- Query3 : List the names of students who have taken course v4 (crsCode).
SELECT name FROM student s, transcript t 
WHERE s.id=t.studId AND crsCode='crsCode896815373';

-- Query 4: List the names of students who have taken a course taught by professor v5 (name). 

select distinct(s.name) from student s , transcript tr, teaching t where s.id=tr.studId and tr.crsCode=t.crsCode and t.profId in 
(select p.id from professor p where p.name=v5);

-- Query5 : List the names of students who have taken a course from department v6 (deptId), but not v7.

SELECT s.name FROM student s, transcript t,course c WHERE s.id=t.studId AND t.crsCode=c.crsCode AND         
c.deptId='deptId606077450' 
NOT IN (SELECT name FROM student s, transcript t,course c WHERE s.id=t.studId AND t.crsCode=c.crsCode AND         
c.deptId = 'deptId71731618');

-- Query 6: List the names of students who have taken all courses offered by department v8 (deptId). 

select s.name from student s where not exists(select c.crsCode from course c where c.deptId = v8 and NOT  exists
(select tr.crsCode from transcript tr where s.id=tr.studId and tr.crsCode=c.crsCode));

/* optimized Query*/

-- Indexing::
create index idx_pname ON professor(name);

create index idx_profId ON teaching("profId");

create index idx_crsCode ON transcript("crsCode");

create index idx_deptId ON course("deptId");

CREATE INDEX idx_sid ON student(id);

-- Query 1 : List the name of the student with id equal to v1 (id).
SELECT s.name FROM student s where s.id = '898275393';
-- Query 2: List the names of students with id in the range of v2 (id) to v3 (inclusive).

select s.name from student s where s.id between v2 and v3;
-- Query 3 : List the names of students who have taken course v4 (crsCode).
SELECT name FROM student s, transcript t 
WHERE s.id=t.studId AND crsCode='crsCode896815373';

-- Query 4: List the names of students who have taken a course taught by professor v5 (name). 

select distinct(s.name) from student s , transcript tr, teaching t where s.id=tr.studId and tr.crsCode=t.crsCode and t.profId in 
(select p.id from professor p where p.name=v5);
-- Query 5 : List the names of students who have taken a course from department v6 (deptId), but not v7.
SELECT s.name FROM student s, transcript t,course c WHERE s.id=t.studId AND t.crsCode=c.crsCode AND         
c.deptId='deptId606077450' 
NOT IN (SELECT name FROM student s, transcript t,course c WHERE s.id=t.studId AND t.crsCode=c.crsCode AND         
c.deptId = 'deptId71731618');

-- Query 6: List the names of students who have taken all courses offered by department v8 (deptId). 

select s.name from student s where not exists(select c.crsCode from course c where c.deptId = v8 and NOT  exists
(select tr.crsCode from transcript tr where s.id=tr.studId and tr.crsCode=c.crsCode));





