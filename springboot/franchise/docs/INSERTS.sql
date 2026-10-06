--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 16.8

-- Started on 2025-03-26 08:43:47

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
-- TOC entry 6 (class 2615 OID 16399)
-- Name: franchise; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA franchise;


ALTER SCHEMA franchise OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16401)
-- Name: franquicia; Type: TABLE; Schema: franchise; Owner: postgres
--

CREATE TABLE franchise.franquicia (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    status integer NOT NULL
);


ALTER TABLE franchise.franquicia OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16400)
-- Name: franquicia_id_seq; Type: SEQUENCE; Schema: franchise; Owner: postgres
--

CREATE SEQUENCE franchise.franquicia_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE franchise.franquicia_id_seq OWNER TO postgres;

--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 216
-- Name: franquicia_id_seq; Type: SEQUENCE OWNED BY; Schema: franchise; Owner: postgres
--

ALTER SEQUENCE franchise.franquicia_id_seq OWNED BY franchise.franquicia.id;


--
-- TOC entry 221 (class 1259 OID 16420)
-- Name: producto; Type: TABLE; Schema: franchise; Owner: postgres
--

CREATE TABLE franchise.producto (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    status integer NOT NULL
);


ALTER TABLE franchise.producto OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16419)
-- Name: producto_id_seq; Type: SEQUENCE; Schema: franchise; Owner: postgres
--

CREATE SEQUENCE franchise.producto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE franchise.producto_id_seq OWNER TO postgres;

--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 220
-- Name: producto_id_seq; Type: SEQUENCE OWNED BY; Schema: franchise; Owner: postgres
--

ALTER SEQUENCE franchise.producto_id_seq OWNED BY franchise.producto.id;


--
-- TOC entry 219 (class 1259 OID 16408)
-- Name: sucursal; Type: TABLE; Schema: franchise; Owner: postgres
--

CREATE TABLE franchise.sucursal (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    status integer NOT NULL,
    franquicia_id integer NOT NULL
);


ALTER TABLE franchise.sucursal OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16407)
-- Name: sucursal_id_seq; Type: SEQUENCE; Schema: franchise; Owner: postgres
--

CREATE SEQUENCE franchise.sucursal_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE franchise.sucursal_id_seq OWNER TO postgres;

--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 218
-- Name: sucursal_id_seq; Type: SEQUENCE OWNED BY; Schema: franchise; Owner: postgres
--

ALTER SEQUENCE franchise.sucursal_id_seq OWNED BY franchise.sucursal.id;


--
-- TOC entry 222 (class 1259 OID 16426)
-- Name: sucursal_x_producto; Type: TABLE; Schema: franchise; Owner: postgres
--

CREATE TABLE franchise.sucursal_x_producto (
    sucursal_id integer NOT NULL,
    producto_id integer NOT NULL,
    stock bigint NOT NULL,
    id integer NOT NULL
);


ALTER TABLE franchise.sucursal_x_producto OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16666)
-- Name: sucursal_x_producto_id_seq; Type: SEQUENCE; Schema: franchise; Owner: postgres
--

CREATE SEQUENCE franchise.sucursal_x_producto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE franchise.sucursal_x_producto_id_seq OWNER TO postgres;

--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 223
-- Name: sucursal_x_producto_id_seq; Type: SEQUENCE OWNED BY; Schema: franchise; Owner: postgres
--

ALTER SEQUENCE franchise.sucursal_x_producto_id_seq OWNED BY franchise.sucursal_x_producto.id;


--
-- TOC entry 4751 (class 2604 OID 16404)
-- Name: franquicia id; Type: DEFAULT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.franquicia ALTER COLUMN id SET DEFAULT nextval('franchise.franquicia_id_seq'::regclass);


--
-- TOC entry 4753 (class 2604 OID 16423)
-- Name: producto id; Type: DEFAULT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.producto ALTER COLUMN id SET DEFAULT nextval('franchise.producto_id_seq'::regclass);


--
-- TOC entry 4752 (class 2604 OID 16411)
-- Name: sucursal id; Type: DEFAULT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal ALTER COLUMN id SET DEFAULT nextval('franchise.sucursal_id_seq'::regclass);


--
-- TOC entry 4754 (class 2604 OID 16667)
-- Name: sucursal_x_producto id; Type: DEFAULT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal_x_producto ALTER COLUMN id SET DEFAULT nextval('franchise.sucursal_x_producto_id_seq'::regclass);


--
-- TOC entry 4914 (class 0 OID 16401)
-- Dependencies: 217
-- Data for Name: franquicia; Type: TABLE DATA; Schema: franchise; Owner: postgres
--

COPY franchise.franquicia (id, nombre, status) FROM stdin;
1	Falabella	1
\.


--
-- TOC entry 4918 (class 0 OID 16420)
-- Dependencies: 221
-- Data for Name: producto; Type: TABLE DATA; Schema: franchise; Owner: postgres
--

COPY franchise.producto (id, nombre, status) FROM stdin;
2	producto2	1
1	producto01	1
3	producto3	1
\.


--
-- TOC entry 4916 (class 0 OID 16408)
-- Dependencies: 219
-- Data for Name: sucursal; Type: TABLE DATA; Schema: franchise; Owner: postgres
--

COPY franchise.sucursal (id, nombre, status, franquicia_id) FROM stdin;
1	Plaza las Américas	1	1
2	Sucursal2	1	1
\.


--
-- TOC entry 4919 (class 0 OID 16426)
-- Dependencies: 222
-- Data for Name: sucursal_x_producto; Type: TABLE DATA; Schema: franchise; Owner: postgres
--

COPY franchise.sucursal_x_producto (sucursal_id, producto_id, stock, id) FROM stdin;
1	1	1	1
2	1	1	3
1	2	300	2
2	3	1	4
\.


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 216
-- Name: franquicia_id_seq; Type: SEQUENCE SET; Schema: franchise; Owner: postgres
--

SELECT pg_catalog.setval('franchise.franquicia_id_seq', 1, true);


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 220
-- Name: producto_id_seq; Type: SEQUENCE SET; Schema: franchise; Owner: postgres
--

SELECT pg_catalog.setval('franchise.producto_id_seq', 3, true);


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 218
-- Name: sucursal_id_seq; Type: SEQUENCE SET; Schema: franchise; Owner: postgres
--

SELECT pg_catalog.setval('franchise.sucursal_id_seq', 2, true);


--
-- TOC entry 4933 (class 0 OID 0)
-- Dependencies: 223
-- Name: sucursal_x_producto_id_seq; Type: SEQUENCE SET; Schema: franchise; Owner: postgres
--

SELECT pg_catalog.setval('franchise.sucursal_x_producto_id_seq', 4, true);


--
-- TOC entry 4756 (class 2606 OID 16406)
-- Name: franquicia franquicia_pkey; Type: CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.franquicia
    ADD CONSTRAINT franquicia_pkey PRIMARY KEY (id);


--
-- TOC entry 4761 (class 2606 OID 16425)
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id);


--
-- TOC entry 4759 (class 2606 OID 16413)
-- Name: sucursal sucursal_pkey; Type: CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal
    ADD CONSTRAINT sucursal_pkey PRIMARY KEY (id);


--
-- TOC entry 4766 (class 2606 OID 16669)
-- Name: sucursal_x_producto sucursal_x_producto_pkey; Type: CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal_x_producto
    ADD CONSTRAINT sucursal_x_producto_pkey PRIMARY KEY (id);


--
-- TOC entry 4757 (class 1259 OID 16441)
-- Name: idx_sucursal_franquicia; Type: INDEX; Schema: franchise; Owner: postgres
--

CREATE INDEX idx_sucursal_franquicia ON franchise.sucursal USING btree (franquicia_id);


--
-- TOC entry 4762 (class 1259 OID 16676)
-- Name: idx_sucursal_producto; Type: INDEX; Schema: franchise; Owner: postgres
--

CREATE UNIQUE INDEX idx_sucursal_producto ON franchise.sucursal_x_producto USING btree (sucursal_id, producto_id);


--
-- TOC entry 4763 (class 1259 OID 16443)
-- Name: idx_sucursal_x_producto_producto; Type: INDEX; Schema: franchise; Owner: postgres
--

CREATE INDEX idx_sucursal_x_producto_producto ON franchise.sucursal_x_producto USING btree (producto_id);


--
-- TOC entry 4764 (class 1259 OID 16442)
-- Name: idx_sucursal_x_producto_sucursal; Type: INDEX; Schema: franchise; Owner: postgres
--

CREATE INDEX idx_sucursal_x_producto_sucursal ON franchise.sucursal_x_producto USING btree (sucursal_id);


--
-- TOC entry 4767 (class 2606 OID 16414)
-- Name: sucursal fk_sucursal_franquicia; Type: FK CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal
    ADD CONSTRAINT fk_sucursal_franquicia FOREIGN KEY (franquicia_id) REFERENCES franchise.franquicia(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4768 (class 2606 OID 16436)
-- Name: sucursal_x_producto fk_sucursal_has_producto_producto; Type: FK CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal_x_producto
    ADD CONSTRAINT fk_sucursal_has_producto_producto FOREIGN KEY (producto_id) REFERENCES franchise.producto(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4769 (class 2606 OID 16431)
-- Name: sucursal_x_producto fk_sucursal_has_producto_sucursal; Type: FK CONSTRAINT; Schema: franchise; Owner: postgres
--

ALTER TABLE ONLY franchise.sucursal_x_producto
    ADD CONSTRAINT fk_sucursal_has_producto_sucursal FOREIGN KEY (sucursal_id) REFERENCES franchise.sucursal(id) ON UPDATE CASCADE ON DELETE RESTRICT;


-- Completed on 2025-03-26 08:43:47

--
-- PostgreSQL database dump complete
--

