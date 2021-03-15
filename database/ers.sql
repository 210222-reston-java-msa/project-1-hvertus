--
-- PostgreSQL database dump
--

-- Dumped from database version 12.6 (Ubuntu 12.6-1.pgdg20.04+1)
-- Dumped by pg_dump version 12.6 (Ubuntu 12.6-1.pgdg20.04+1)

-- Started on 2021-03-01 20:23:28 CST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 123731)
-- Name: ers_admin; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA ers_admin;


ALTER SCHEMA ers_admin OWNER TO postgres;

--
-- TOC entry 215 (class 1255 OID 123732)
-- Name: authenticate_user(character varying, character varying); Type: FUNCTION; Schema: ers_admin; Owner: postgres
--

CREATE FUNCTION ers_admin.authenticate_user(user_name character varying, pwd character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
DECLARE selectedUser varchar;
DECLARE selectedRole varchar;
DECLARE roleKey int;
BEGIN
	select username, role_key from ers_admin.user_store into selectedUser, roleKey   
	where username = user_name and "password" = pwd;

	if selectedUser is not null then
		select name from ers_admin.role into selectedRole 
		where role_key = roleKey;			
	end if;	
	
	return selectedRole;
END;$$;


ALTER FUNCTION ers_admin.authenticate_user(user_name character varying, pwd character varying) OWNER TO postgres;

--
-- TOC entry 216 (class 1255 OID 123925)
-- Name: create_user(character varying, character varying, character varying); Type: FUNCTION; Schema: ers_admin; Owner: postgres
--

CREATE FUNCTION ers_admin.create_user(user_name character varying, pwd character varying, rolename character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
DECLARE selectedUser varchar;
DECLARE selectedRole int;
BEGIN
	select username from ers_admin.user_store into selectedUser 
	where username = user_name;

	--if not found then
	if selectedUser is null  then
		select role_key from ers_admin.role into selectedRole 
		where name = roleName;
			
		insert into ers_admin.user_store(username, "password", role_key)
		values(user_name, pwd, selectedRole);				
	end if;	
	
	return selectedUser;
END;$$;


ALTER FUNCTION ers_admin.create_user(user_name character varying, pwd character varying, rolename character varying) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 206 (class 1259 OID 123773)
-- Name: employee; Type: TABLE; Schema: ers_admin; Owner: postgres
--

CREATE TABLE ers_admin.employee (
    employee_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    gender character varying(10) NOT NULL,
    national_id character varying(50) NOT NULL,
    address character varying(100) NOT NULL,
    email character varying(50) NOT NULL,
    phone character varying(50) NOT NULL,
    picture bytea,
    user_id bigint NOT NULL,
    created_date date NOT NULL,
    date_stamp date DEFAULT CURRENT_DATE,
    created_time time without time zone NOT NULL,
    time_stamp time without time zone DEFAULT CURRENT_TIME,
    created_by character varying(50),
    user_stamp character varying(50)
);


ALTER TABLE ers_admin.employee OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 123771)
-- Name: employee_employee_key_seq; Type: SEQUENCE; Schema: ers_admin; Owner: postgres
--

CREATE SEQUENCE ers_admin.employee_employee_key_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ers_admin.employee_employee_key_seq OWNER TO postgres;

--
-- TOC entry 3041 (class 0 OID 0)
-- Dependencies: 205
-- Name: employee_employee_key_seq; Type: SEQUENCE OWNED BY; Schema: ers_admin; Owner: postgres
--

ALTER SEQUENCE ers_admin.employee_employee_key_seq OWNED BY ers_admin.employee.employee_id;


--
-- TOC entry 212 (class 1259 OID 123877)
-- Name: reimbursement; Type: TABLE; Schema: ers_admin; Owner: postgres
--

CREATE TABLE ers_admin.reimbursement (
    reimbursement_id integer NOT NULL,
    amount numeric(50,2) DEFAULT 0,
    submitted timestamp without time zone NOT NULL,
    resolved timestamp without time zone NOT NULL,
    description character varying(250),
    receipt bytea,
    author integer,
    resolver integer,
    status_id integer,
    type_id integer,
    created_date date NOT NULL,
    date_stamp date DEFAULT CURRENT_DATE,
    created_time time without time zone NOT NULL,
    time_stamp time without time zone DEFAULT CURRENT_TIME,
    created_by character varying(50),
    user_stamp character varying(50)
);


ALTER TABLE ers_admin.reimbursement OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 123875)
-- Name: reimbursement_reimbursement_id_seq; Type: SEQUENCE; Schema: ers_admin; Owner: postgres
--

CREATE SEQUENCE ers_admin.reimbursement_reimbursement_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ers_admin.reimbursement_reimbursement_id_seq OWNER TO postgres;

--
-- TOC entry 3042 (class 0 OID 0)
-- Dependencies: 211
-- Name: reimbursement_reimbursement_id_seq; Type: SEQUENCE OWNED BY; Schema: ers_admin; Owner: postgres
--

ALTER SEQUENCE ers_admin.reimbursement_reimbursement_id_seq OWNED BY ers_admin.reimbursement.reimbursement_id;


--
-- TOC entry 208 (class 1259 OID 123789)
-- Name: reimbursement_status; Type: TABLE; Schema: ers_admin; Owner: postgres
--

CREATE TABLE ers_admin.reimbursement_status (
    status_id integer NOT NULL,
    status character varying(10) NOT NULL
);


ALTER TABLE ers_admin.reimbursement_status OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 123787)
-- Name: reimbursement_status_status_id_seq; Type: SEQUENCE; Schema: ers_admin; Owner: postgres
--

CREATE SEQUENCE ers_admin.reimbursement_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ers_admin.reimbursement_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 3043 (class 0 OID 0)
-- Dependencies: 207
-- Name: reimbursement_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: ers_admin; Owner: postgres
--

ALTER SEQUENCE ers_admin.reimbursement_status_status_id_seq OWNED BY ers_admin.reimbursement_status.status_id;


--
-- TOC entry 210 (class 1259 OID 123799)
-- Name: reimbursement_type; Type: TABLE; Schema: ers_admin; Owner: postgres
--

CREATE TABLE ers_admin.reimbursement_type (
    type_id integer NOT NULL,
    type character varying(10) NOT NULL
);


ALTER TABLE ers_admin.reimbursement_type OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 123797)
-- Name: reimbursement_type_type_id_seq; Type: SEQUENCE; Schema: ers_admin; Owner: postgres
--

CREATE SEQUENCE ers_admin.reimbursement_type_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ers_admin.reimbursement_type_type_id_seq OWNER TO postgres;

--
-- TOC entry 3044 (class 0 OID 0)
-- Dependencies: 209
-- Name: reimbursement_type_type_id_seq; Type: SEQUENCE OWNED BY; Schema: ers_admin; Owner: postgres
--

ALTER SEQUENCE ers_admin.reimbursement_type_type_id_seq OWNED BY ers_admin.reimbursement_type.type_id;


--
-- TOC entry 214 (class 1259 OID 123914)
-- Name: role; Type: TABLE; Schema: ers_admin; Owner: postgres
--

CREATE TABLE ers_admin.role (
    role_id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE ers_admin.role OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 123912)
-- Name: role_role_key_seq; Type: SEQUENCE; Schema: ers_admin; Owner: postgres
--

CREATE SEQUENCE ers_admin.role_role_key_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ers_admin.role_role_key_seq OWNER TO postgres;

--
-- TOC entry 3045 (class 0 OID 0)
-- Dependencies: 213
-- Name: role_role_key_seq; Type: SEQUENCE OWNED BY; Schema: ers_admin; Owner: postgres
--

ALTER SEQUENCE ers_admin.role_role_key_seq OWNED BY ers_admin.role.role_id;


--
-- TOC entry 204 (class 1259 OID 123741)
-- Name: user_store; Type: TABLE; Schema: ers_admin; Owner: postgres
--

CREATE TABLE ers_admin.user_store (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE ers_admin.user_store OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 123739)
-- Name: user_store_user_key_seq; Type: SEQUENCE; Schema: ers_admin; Owner: postgres
--

CREATE SEQUENCE ers_admin.user_store_user_key_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ers_admin.user_store_user_key_seq OWNER TO postgres;

--
-- TOC entry 3046 (class 0 OID 0)
-- Dependencies: 203
-- Name: user_store_user_key_seq; Type: SEQUENCE OWNED BY; Schema: ers_admin; Owner: postgres
--

ALTER SEQUENCE ers_admin.user_store_user_key_seq OWNED BY ers_admin.user_store.user_id;


--
-- TOC entry 2866 (class 2604 OID 123776)
-- Name: employee employee_id; Type: DEFAULT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.employee ALTER COLUMN employee_id SET DEFAULT nextval('ers_admin.employee_employee_key_seq'::regclass);


--
-- TOC entry 2871 (class 2604 OID 123880)
-- Name: reimbursement reimbursement_id; Type: DEFAULT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement ALTER COLUMN reimbursement_id SET DEFAULT nextval('ers_admin.reimbursement_reimbursement_id_seq'::regclass);


--
-- TOC entry 2869 (class 2604 OID 123792)
-- Name: reimbursement_status status_id; Type: DEFAULT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement_status ALTER COLUMN status_id SET DEFAULT nextval('ers_admin.reimbursement_status_status_id_seq'::regclass);


--
-- TOC entry 2870 (class 2604 OID 123802)
-- Name: reimbursement_type type_id; Type: DEFAULT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement_type ALTER COLUMN type_id SET DEFAULT nextval('ers_admin.reimbursement_type_type_id_seq'::regclass);


--
-- TOC entry 2875 (class 2604 OID 123917)
-- Name: role role_id; Type: DEFAULT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.role ALTER COLUMN role_id SET DEFAULT nextval('ers_admin.role_role_key_seq'::regclass);


--
-- TOC entry 2865 (class 2604 OID 123744)
-- Name: user_store user_id; Type: DEFAULT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.user_store ALTER COLUMN user_id SET DEFAULT nextval('ers_admin.user_store_user_key_seq'::regclass);


--
-- TOC entry 3027 (class 0 OID 123773)
-- Dependencies: 206
-- Data for Name: employee; Type: TABLE DATA; Schema: ers_admin; Owner: postgres
--

COPY ers_admin.employee (employee_id, first_name, last_name, gender, national_id, address, email, phone, picture, user_id, created_date, date_stamp, created_time, time_stamp, created_by, user_stamp) FROM stdin;
\.


--
-- TOC entry 3033 (class 0 OID 123877)
-- Dependencies: 212
-- Data for Name: reimbursement; Type: TABLE DATA; Schema: ers_admin; Owner: postgres
--

COPY ers_admin.reimbursement (reimbursement_id, amount, submitted, resolved, description, receipt, author, resolver, status_id, type_id, created_date, date_stamp, created_time, time_stamp, created_by, user_stamp) FROM stdin;
\.


--
-- TOC entry 3029 (class 0 OID 123789)
-- Dependencies: 208
-- Data for Name: reimbursement_status; Type: TABLE DATA; Schema: ers_admin; Owner: postgres
--

COPY ers_admin.reimbursement_status (status_id, status) FROM stdin;
1	approved
2	denied
3	pending
\.


--
-- TOC entry 3031 (class 0 OID 123799)
-- Dependencies: 210
-- Data for Name: reimbursement_type; Type: TABLE DATA; Schema: ers_admin; Owner: postgres
--

COPY ers_admin.reimbursement_type (type_id, type) FROM stdin;
1	lodging
2	travel
3	food
\.


--
-- TOC entry 3035 (class 0 OID 123914)
-- Dependencies: 214
-- Data for Name: role; Type: TABLE DATA; Schema: ers_admin; Owner: postgres
--

COPY ers_admin.role (role_id, name) FROM stdin;
1	employee
2	manager
\.


--
-- TOC entry 3025 (class 0 OID 123741)
-- Dependencies: 204
-- Data for Name: user_store; Type: TABLE DATA; Schema: ers_admin; Owner: postgres
--

COPY ers_admin.user_store (user_id, username, password, role_id) FROM stdin;
\.


--
-- TOC entry 3047 (class 0 OID 0)
-- Dependencies: 205
-- Name: employee_employee_key_seq; Type: SEQUENCE SET; Schema: ers_admin; Owner: postgres
--

SELECT pg_catalog.setval('ers_admin.employee_employee_key_seq', 1, false);


--
-- TOC entry 3048 (class 0 OID 0)
-- Dependencies: 211
-- Name: reimbursement_reimbursement_id_seq; Type: SEQUENCE SET; Schema: ers_admin; Owner: postgres
--

SELECT pg_catalog.setval('ers_admin.reimbursement_reimbursement_id_seq', 1, false);


--
-- TOC entry 3049 (class 0 OID 0)
-- Dependencies: 207
-- Name: reimbursement_status_status_id_seq; Type: SEQUENCE SET; Schema: ers_admin; Owner: postgres
--

SELECT pg_catalog.setval('ers_admin.reimbursement_status_status_id_seq', 4, true);


--
-- TOC entry 3050 (class 0 OID 0)
-- Dependencies: 209
-- Name: reimbursement_type_type_id_seq; Type: SEQUENCE SET; Schema: ers_admin; Owner: postgres
--

SELECT pg_catalog.setval('ers_admin.reimbursement_type_type_id_seq', 3, true);


--
-- TOC entry 3051 (class 0 OID 0)
-- Dependencies: 213
-- Name: role_role_key_seq; Type: SEQUENCE SET; Schema: ers_admin; Owner: postgres
--

SELECT pg_catalog.setval('ers_admin.role_role_key_seq', 2, true);


--
-- TOC entry 3052 (class 0 OID 0)
-- Dependencies: 203
-- Name: user_store_user_key_seq; Type: SEQUENCE SET; Schema: ers_admin; Owner: postgres
--

SELECT pg_catalog.setval('ers_admin.user_store_user_key_seq', 1, false);


--
-- TOC entry 2879 (class 2606 OID 123781)
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (employee_id);


--
-- TOC entry 2891 (class 2606 OID 123919)
-- Name: role pkey_role_id; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.role
    ADD CONSTRAINT pkey_role_id PRIMARY KEY (role_id);


--
-- TOC entry 2889 (class 2606 OID 123886)
-- Name: reimbursement reimbursement_pkey; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement
    ADD CONSTRAINT reimbursement_pkey PRIMARY KEY (reimbursement_id);


--
-- TOC entry 2881 (class 2606 OID 123794)
-- Name: reimbursement_status reimbursement_status_pkey; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement_status
    ADD CONSTRAINT reimbursement_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 2883 (class 2606 OID 123796)
-- Name: reimbursement_status reimbursement_status_status_key; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement_status
    ADD CONSTRAINT reimbursement_status_status_key UNIQUE (status);


--
-- TOC entry 2885 (class 2606 OID 123804)
-- Name: reimbursement_type reimbursement_type_pkey; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement_type
    ADD CONSTRAINT reimbursement_type_pkey PRIMARY KEY (type_id);


--
-- TOC entry 2887 (class 2606 OID 123806)
-- Name: reimbursement_type reimbursement_type_type_key; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement_type
    ADD CONSTRAINT reimbursement_type_type_key UNIQUE (type);


--
-- TOC entry 2877 (class 2606 OID 123752)
-- Name: user_store user_pkey; Type: CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.user_store
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 2894 (class 2606 OID 123887)
-- Name: reimbursement fk_employee_author; Type: FK CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement
    ADD CONSTRAINT fk_employee_author FOREIGN KEY (author) REFERENCES ers_admin.employee(employee_id);


--
-- TOC entry 2895 (class 2606 OID 123892)
-- Name: reimbursement fk_employee_resolver; Type: FK CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement
    ADD CONSTRAINT fk_employee_resolver FOREIGN KEY (resolver) REFERENCES ers_admin.employee(employee_id);


--
-- TOC entry 2896 (class 2606 OID 123897)
-- Name: reimbursement fk_reimbursement_status; Type: FK CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement
    ADD CONSTRAINT fk_reimbursement_status FOREIGN KEY (status_id) REFERENCES ers_admin.reimbursement_status(status_id);


--
-- TOC entry 2897 (class 2606 OID 123902)
-- Name: reimbursement fk_reimbursement_type; Type: FK CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.reimbursement
    ADD CONSTRAINT fk_reimbursement_type FOREIGN KEY (type_id) REFERENCES ers_admin.reimbursement_type(type_id);


--
-- TOC entry 2892 (class 2606 OID 123920)
-- Name: user_store fk_role_id; Type: FK CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.user_store
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES ers_admin.role(role_id);


--
-- TOC entry 2893 (class 2606 OID 123782)
-- Name: employee fk_user_id; Type: FK CONSTRAINT; Schema: ers_admin; Owner: postgres
--

ALTER TABLE ONLY ers_admin.employee
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES ers_admin.user_store(user_id);


-- Completed on 2021-03-01 20:23:28 CST

--
-- PostgreSQL database dump complete
--

