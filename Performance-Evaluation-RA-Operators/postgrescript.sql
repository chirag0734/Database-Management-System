-- Table: public.student

-- DROP TABLE public.student;

CREATE TABLE public.student
(
    id integer NOT NULL,
    name character varying COLLATE pg_catalog."default",
    address character varying COLLATE pg_catalog."default",
    status character varying COLLATE pg_catalog."default",
    CONSTRAINT student_pkey PRIMARY KEY (id)
)



-- Table: public.course

-- DROP TABLE public.course;

CREATE TABLE public.course
(
    "crsCode" character varying COLLATE pg_catalog."default" NOT NULL,
    "deptId" character varying COLLATE pg_catalog."default",
    "crsName" character varying COLLATE pg_catalog."default",
    descr character varying COLLATE pg_catalog."default",
    CONSTRAINT course_pkey PRIMARY KEY ("crsCode")
)




-- Table: public.professor

-- DROP TABLE public.professor;

CREATE TABLE public.professor
(
    id integer NOT NULL,
    name character varying COLLATE pg_catalog."default",
    "deptId" character varying COLLATE pg_catalog."default",
    CONSTRAINT professor_pkey PRIMARY KEY (id)
)



-- Table: public.teaching

-- DROP TABLE public.teaching;

CREATE TABLE public.teaching
(
    "crsCode" character varying COLLATE pg_catalog."default" NOT NULL,
    semester character varying COLLATE pg_catalog."default" NOT NULL,
    "profId" integer,
    CONSTRAINT teaching_pkey PRIMARY KEY ("crsCode", semester)
)




-- Table: public.transcript

-- DROP TABLE public.transcript;

CREATE TABLE public.transcript
(
    "studId" integer NOT NULL,
    "crsCode" character varying COLLATE pg_catalog."default" NOT NULL,
    semester character varying COLLATE pg_catalog."default" NOT NULL,
    grade character varying COLLATE pg_catalog."default",
    CONSTRAINT transcript_pkey PRIMARY KEY ("studId", "crsCode", semester)
)





DELETE FROM public.student;
DELETE FROM public.course;
DELETE FROM public.professor;
DELETE FROM public.teaching;
DELETE FROM public.transcript;