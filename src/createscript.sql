--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2024-02-11 22:31:44

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 27247)
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(100) NOT NULL,
    city character varying(100) NOT NULL,
    postal_code character varying(10) NOT NULL,
    ico_value character varying(12),
    dic_value character varying(16),
    is_root boolean DEFAULT false
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 27251)
-- Name: customer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.customer ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 217 (class 1259 OID 27252)
-- Name: customer_reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_reservation (
    id integer NOT NULL,
    id_customer integer NOT NULL,
    reserved_from date NOT NULL,
    reserved_until date NOT NULL,
    id_position character varying(5) NOT NULL
);


ALTER TABLE public.customer_reservation OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 27255)
-- Name: customer_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.customer_reservation ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customer_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 219 (class 1259 OID 27256)
-- Name: history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.history (
    id integer NOT NULL,
    id_customer integer NOT NULL,
    "time" time without time zone NOT NULL,
    date date NOT NULL,
    truck_income boolean DEFAULT true NOT NULL,
    number_of_pallets integer NOT NULL,
    truck_number integer NOT NULL
);


ALTER TABLE public.history OWNER TO postgres;

--
-- TOC entry 4944 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN history.truck_income; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.history.truck_income IS 'poznamka, ci kamion prisiel s paletami alebo odišiel
';


--
-- TOC entry 4945 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN history.truck_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.history.truck_number IS 'číslo točky';


--
-- TOC entry 220 (class 1259 OID 27260)
-- Name: history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.history ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 221 (class 1259 OID 27261)
-- Name: material; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.material (
    id integer NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.material OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 27264)
-- Name: material_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.material ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.material_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 223 (class 1259 OID 27265)
-- Name: pallet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pallet (
    pnr character varying(15) NOT NULL,
    date_income date NOT NULL,
    is_damaged boolean DEFAULT false NOT NULL,
    id_user integer,
    type character varying(50) DEFAULT 'europaleta'::character varying NOT NULL,
    note text,
    weight double precision NOT NULL,
    number_of_positions integer NOT NULL
);


ALTER TABLE public.pallet OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 27272)
-- Name: pallet_on_position; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pallet_on_position (
    id integer NOT NULL,
    id_pallet character varying(15) NOT NULL,
    id_position character varying(5) NOT NULL
);


ALTER TABLE public.pallet_on_position OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 27275)
-- Name: pallet_on_position_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.pallet_on_position ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pallet_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 27276)
-- Name: position; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."position" (
    name character varying(5) NOT NULL,
    is_tall boolean DEFAULT false NOT NULL
);


ALTER TABLE public."position" OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 27280)
-- Name: stored_on_pallet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stored_on_pallet (
    id integer NOT NULL,
    pnr character varying(15) NOT NULL,
    id_product integer NOT NULL,
    quantity integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.stored_on_pallet OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 27284)
-- Name: stored_on_position_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.stored_on_pallet ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.stored_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 229 (class 1259 OID 27285)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    password character varying(20),
    admin boolean
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 27288)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 4923 (class 0 OID 27247)
-- Dependencies: 215
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer (id, name, address, city, postal_code, ico_value, dic_value, is_root) FROM stdin;
1	Gefco Slovakia s.r.o.	SNP 811/168	Strečno	013 24	\N	\N	t
\.


--
-- TOC entry 4925 (class 0 OID 27252)
-- Dependencies: 217
-- Data for Name: customer_reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer_reservation (id, id_customer, reserved_from, reserved_until, id_position) FROM stdin;
\.


--
-- TOC entry 4927 (class 0 OID 27256)
-- Dependencies: 219
-- Data for Name: history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.history (id, id_customer, "time", date, truck_income, number_of_pallets, truck_number) FROM stdin;
\.


--
-- TOC entry 4929 (class 0 OID 27261)
-- Dependencies: 221
-- Data for Name: material; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.material (id, name) FROM stdin;
\.


--
-- TOC entry 4931 (class 0 OID 27265)
-- Dependencies: 223
-- Data for Name: pallet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pallet (pnr, date_income, is_damaged, id_user, type, note, weight, number_of_positions) FROM stdin;
\.


--
-- TOC entry 4932 (class 0 OID 27272)
-- Dependencies: 224
-- Data for Name: pallet_on_position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pallet_on_position (id, id_pallet, id_position) FROM stdin;
\.


--
-- TOC entry 4934 (class 0 OID 27276)
-- Dependencies: 226
-- Data for Name: position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."position" (name, is_tall) FROM stdin;
A0475	f
A0455	f
A0435	f
A0415	f
A0395	f
A0375	f
A0355	f
A0335	f
A0315	f
A0295	f
A0275	f
A0255	f
A0235	f
A0215	f
A0195	f
A0175	f
A0155	f
A0135	f
A0115	f
A0095	f
A0075	f
A0055	f
A0035	f
A0015	f
A0474	f
A0454	f
A0434	f
A0414	f
A0394	f
A0374	f
A0354	f
A0334	f
A0314	f
A0294	f
A0274	f
A0254	f
A0234	f
A0214	f
A0194	f
A0174	f
A0154	f
A0134	f
A0114	f
A0094	f
A0074	f
A0054	f
A0034	f
A0014	f
A0473	f
A0453	f
A0433	f
A0413	f
A0393	f
A0373	f
A0353	f
A0333	f
A0313	f
A0293	f
A0273	f
A0253	f
A0233	f
A0213	f
A0193	f
A0173	f
A0153	f
A0133	f
A0113	f
A0093	f
A0073	f
A0053	f
A0033	f
A0013	f
A0472	f
A0452	f
A0432	f
A0412	f
A0392	f
A0372	f
A0352	f
A0332	f
A0312	f
A0292	f
A0272	f
A0252	f
A0232	f
A0212	f
A0192	f
A0172	f
A0152	f
A0132	f
A0112	f
A0092	f
A0072	f
A0052	f
A0032	f
A0012	f
A0471	f
A0451	f
A0431	f
A0411	f
A0391	f
A0371	f
A0351	f
A0331	f
A0231	f
A0211	f
A0191	f
A0171	f
A0151	f
A0131	f
A0111	f
A0091	f
A0071	f
A0051	f
A0031	f
A0011	f
A0470	f
A0450	f
A0430	f
A0410	f
A0390	f
A0370	f
A0350	f
A0330	f
A0270	f
A0250	f
A0230	f
A0210	f
A0190	f
A0170	f
A0150	f
A0130	f
A0110	f
A0090	f
A0070	f
A0050	f
A0030	f
A0010	f
A0325	f
A0305	f
A0285	f
A0265	f
A0245	f
A0225	f
A0205	f
A0185	f
A0165	f
A0145	f
A0125	f
A0105	f
A0085	f
A0065	f
A0045	f
A0025	f
A0324	f
A0304	f
A0284	f
A0264	f
A0244	f
A0224	f
A0204	f
A0184	f
A0164	f
A0144	f
A0124	f
A0104	f
A0084	f
A0064	f
A0044	f
A0024	f
A0323	f
A0303	f
A0283	f
A0263	f
A0243	f
A0223	f
A0203	f
A0183	f
A0163	f
A0143	f
A0123	f
A0103	f
A0083	f
A0063	f
A0043	f
A0023	f
A0322	f
A0302	f
A0282	f
A0262	f
A0242	f
A0222	f
A0202	f
A0182	f
A0162	f
A0142	f
A0122	f
A0102	f
A0082	f
A0062	f
A0042	f
A0022	f
A0321	f
A0301	f
A0281	f
A0261	f
A0241	f
A0221	f
A0201	f
A0181	f
A0161	f
A0141	f
A0121	f
A0101	f
A0081	f
A0061	f
A0041	f
A0021	f
A0320	f
A0300	f
A0280	f
A0260	f
A0240	f
A0220	f
A0200	f
A0180	f
A0160	f
A0140	f
A0120	f
A0100	f
A0080	f
A0060	f
A0040	f
A0020	f
B0475	f
B0455	f
B0435	f
B0415	f
B0395	f
B0375	f
B0355	f
B0335	f
B0315	f
B0295	f
B0275	f
B0255	f
B0235	f
B0215	f
B0195	f
B0175	f
B0155	f
B0135	f
B0115	f
B0095	f
B0075	f
B0055	f
B0035	f
B0015	f
B0474	f
B0454	f
B0434	f
B0414	f
B0394	f
B0374	f
B0354	f
B0334	f
B0314	f
B0294	f
B0274	f
B0254	f
B0234	f
B0214	f
B0194	f
B0174	f
B0154	f
B0134	f
B0114	f
B0094	f
B0074	f
B0054	f
B0034	f
B0014	f
B0473	f
B0453	f
B0433	f
B0413	f
B0393	f
B0373	f
B0353	f
B0333	f
B0313	f
B0293	f
B0273	f
B0253	f
B0233	f
B0213	f
B0193	f
B0173	f
B0153	f
B0133	f
B0113	f
B0093	f
B0073	f
B0053	f
B0033	f
B0013	f
B0472	f
B0452	f
B0432	f
B0412	f
B0392	f
B0372	f
B0352	f
B0332	f
B0312	f
B0292	f
B0272	f
B0252	f
B0232	f
B0212	f
B0192	f
B0172	f
B0152	f
B0132	f
B0112	f
B0092	f
B0072	f
B0052	f
B0032	f
B0012	f
B0471	f
B0451	f
B0431	f
B0411	f
B0391	f
B0371	f
B0351	f
B0331	f
B0311	f
B0291	f
B0271	f
B0251	f
B0231	f
B0211	f
B0191	f
B0171	f
B0151	f
B0131	f
B0111	f
B0091	f
B0071	f
B0051	f
B0031	f
B0011	f
B0470	f
B0450	f
B0430	f
B0410	f
B0390	f
B0370	f
B0350	f
B0330	f
B0310	f
B0290	f
B0270	f
B0250	f
B0230	f
B0210	f
B0190	f
B0170	f
B0150	f
B0130	f
B0110	f
B0090	f
B0070	f
B0050	f
B0030	f
B0010	f
B0485	f
B0465	f
B0445	f
B0425	f
B0405	f
B0385	f
B0365	f
B0345	f
B0325	f
B0305	f
B0285	f
B0265	f
B0245	f
B0225	f
B0205	f
B0185	f
B0165	f
B0145	f
B0125	f
B0105	f
B0085	f
B0065	f
B0045	f
B0025	f
B0484	f
B0464	f
B0444	f
B0424	f
B0404	f
B0384	f
B0364	f
B0344	f
B0324	f
B0304	f
B0284	f
B0264	f
B0244	f
B0224	f
B0204	f
B0184	f
B0164	f
B0144	f
B0124	f
B0104	f
B0084	f
B0064	f
B0044	f
B0024	f
B0483	f
B0463	f
B0443	f
B0423	f
B0403	f
B0383	f
B0363	f
B0343	f
B0323	f
B0303	f
B0283	f
B0263	f
B0243	f
B0223	f
B0203	f
B0183	f
B0163	f
B0143	f
B0123	f
B0103	f
B0083	f
B0063	f
B0043	f
B0023	f
B0482	f
B0462	f
B0442	f
B0422	f
B0402	f
B0382	f
B0362	f
B0342	f
B0322	f
B0302	f
B0282	f
B0262	f
B0242	f
B0222	f
B0202	f
B0182	f
B0162	f
B0142	f
B0122	f
B0102	f
B0082	f
B0062	f
B0042	f
B0022	f
B0481	f
B0461	f
B0441	f
B0421	f
B0401	f
B0381	f
B0361	f
B0341	f
B0321	f
B0301	f
B0281	f
B0261	f
B0241	f
B0221	f
B0201	f
B0181	f
B0161	f
B0141	f
B0121	f
B0101	f
B0081	f
B0061	f
B0041	f
B0021	f
B0480	f
B0460	f
B0440	f
B0420	f
B0400	f
B0380	f
B0360	f
B0340	f
B0320	f
B0300	f
B0280	f
B0260	f
B0240	f
B0220	f
B0200	f
B0180	f
B0160	f
B0140	f
B0120	f
B0100	f
B0080	f
B0060	f
B0040	f
B0020	f
C0475	f
C0455	f
C0435	f
C0415	f
C0395	f
C0375	f
C0355	f
C0335	f
C0315	f
C0295	f
C0275	f
C0255	f
C0235	f
C0215	f
C0195	f
C0175	f
C0155	f
C0135	f
C0115	f
C0095	f
C0075	f
C0055	f
C0035	f
C0015	f
C0474	f
C0454	f
C0434	f
C0414	f
C0394	f
C0374	f
C0354	f
C0334	f
C0314	f
C0294	f
C0274	f
C0254	f
C0234	f
C0214	f
C0194	f
C0174	f
C0154	f
C0134	f
C0114	f
C0094	f
C0074	f
C0054	f
C0034	f
C0014	f
C0473	f
C0453	f
C0433	f
C0413	f
C0393	f
C0373	f
C0353	f
C0333	f
C0313	f
C0293	f
C0273	f
C0253	f
C0233	f
C0213	f
C0193	f
C0173	f
C0153	f
C0133	f
C0113	f
C0093	f
C0073	f
C0053	f
C0033	f
C0013	f
C0472	f
C0452	f
C0432	f
C0412	f
C0392	f
C0372	f
C0352	f
C0332	f
C0312	f
C0292	f
C0272	f
C0252	f
C0232	f
C0212	f
C0192	f
C0172	f
C0152	f
C0132	f
C0112	f
C0092	f
C0072	f
C0052	f
C0032	f
C0012	f
C0471	f
C0451	f
C0431	f
C0411	f
C0391	f
C0371	f
C0351	f
C0331	f
C0311	f
C0291	f
C0271	f
C0251	f
C0231	f
C0211	f
C0191	f
C0171	f
C0151	f
C0131	f
C0111	f
C0091	f
C0071	f
C0051	f
C0031	f
C0011	f
C0470	f
C0450	f
C0430	f
C0410	f
C0390	f
C0370	f
C0350	f
C0330	f
C0310	f
C0290	f
C0270	f
C0250	f
C0230	f
C0210	f
C0190	f
C0170	f
C0150	f
C0130	f
C0110	f
C0090	f
C0070	f
C0050	f
C0030	f
C0010	f
C0645	f
C0625	f
C0605	f
C0585	f
C0565	f
C0545	f
C0525	f
C0505	f
C0485	f
C0465	f
C0445	f
C0425	f
C0405	f
C0385	f
C0365	f
C0345	f
C0325	f
C0305	f
C0285	f
C0265	f
C0245	f
C0225	f
C0205	f
C0185	f
C0165	f
C0145	f
C0125	f
C0105	f
C0085	f
C0065	f
C0045	f
C0025	f
C0644	f
C0624	f
C0604	f
C0584	f
C0564	f
C0544	f
C0524	f
C0504	f
C0484	f
C0464	f
C0444	f
C0424	f
C0404	f
C0384	f
C0364	f
C0344	f
C0324	f
C0304	f
C0284	f
C0264	f
C0244	f
C0224	f
C0204	f
C0184	f
C0164	f
C0144	f
C0124	f
C0104	f
C0084	f
C0064	f
C0044	f
C0024	f
C0643	f
C0623	f
C0603	f
C0583	f
C0563	f
C0543	f
C0523	f
C0503	f
C0483	f
C0463	f
C0443	f
C0423	f
C0403	f
C0383	f
C0363	f
C0343	f
C0323	f
C0303	f
C0283	f
C0263	f
C0243	f
C0223	f
C0203	f
C0183	f
C0163	f
C0143	f
C0123	f
C0103	f
C0083	f
C0063	f
C0043	f
C0023	f
C0641	t
C0621	t
C0601	t
C0581	t
C0561	t
C0541	t
C0521	t
C0501	t
C0481	t
C0461	t
C0441	t
C0421	t
C0401	t
C0381	t
C0361	t
C0341	t
C0321	t
C0301	t
C0281	t
C0261	t
C0241	t
C0221	t
C0201	t
C0181	t
C0161	t
C0141	t
C0121	t
C0101	t
C0081	t
C0061	t
C0041	t
C0021	t
C0640	t
C0620	t
C0600	t
C0580	t
C0560	t
C0540	t
C0520	t
C0500	t
C0480	t
C0460	t
C0440	t
C0420	t
C0400	t
C0380	t
C0360	t
C0340	t
C0320	t
C0300	t
C0280	t
C0260	t
C0240	t
C0220	t
C0200	t
C0180	t
C0160	t
C0140	t
C0120	t
C0100	t
C0080	t
C0060	t
C0040	t
C0020	t
D0635	f
D0615	f
D0595	f
D0575	f
D0555	f
D0535	f
D0515	f
D0495	f
D0475	f
D0455	f
D0435	f
D0415	f
D0395	f
D0375	f
D0355	f
D0335	f
D0315	f
D0295	f
D0275	f
D0255	f
D0235	f
D0215	f
D0195	f
D0175	f
D0155	f
D0135	f
D0115	f
D0095	f
D0075	f
D0055	f
D0035	f
D0015	f
D0634	f
D0614	f
D0594	f
D0574	f
D0554	f
D0534	f
D0514	f
D0494	f
D0474	f
D0454	f
D0434	f
D0414	f
D0394	f
D0374	f
D0354	f
D0334	f
D0314	f
D0294	f
D0274	f
D0254	f
D0234	f
D0214	f
D0194	f
D0174	f
D0154	f
D0134	f
D0114	f
D0094	f
D0074	f
D0054	f
D0034	f
D0014	f
D0633	f
D0613	f
D0593	f
D0573	f
D0553	f
D0533	f
D0513	f
D0493	f
D0473	f
D0453	f
D0433	f
D0413	f
D0393	f
D0373	f
D0353	f
D0333	f
D0313	f
D0293	f
D0273	f
D0253	f
D0233	f
D0213	f
D0193	f
D0173	f
D0153	f
D0133	f
D0113	f
D0093	f
D0073	f
D0053	f
D0033	f
D0013	f
D0631	t
D0611	t
D0591	t
D0571	t
D0551	t
D0531	t
D0511	t
D0491	t
D0471	t
D0451	t
D0431	t
D0411	t
D0391	t
D0371	t
D0351	t
D0331	t
D0311	t
D0291	t
D0271	t
D0251	t
D0231	t
D0211	t
D0191	t
D0171	t
D0151	t
D0131	t
D0111	t
D0091	t
D0071	t
D0051	t
D0031	t
D0011	t
D0630	t
D0610	t
D0590	t
D0570	t
D0550	t
D0530	t
D0510	t
D0490	t
D0470	t
D0450	t
D0430	t
D0410	t
D0390	t
D0370	t
D0350	t
D0330	t
D0310	t
D0290	t
D0270	t
D0250	t
D0230	t
D0210	t
D0190	t
D0170	t
D0150	t
D0130	t
D0110	t
D0090	t
D0070	t
D0050	t
D0030	t
D0010	t
D0485	f
D0465	f
D0445	f
D0425	f
D0405	f
D0385	f
D0365	f
D0345	f
D0325	f
D0305	f
D0285	f
D0265	f
D0245	f
D0225	f
D0205	f
D0185	f
D0165	f
D0145	f
D0125	f
D0105	f
D0085	f
D0065	f
D0045	f
D0025	f
D0484	f
D0464	f
D0444	f
D0424	f
D0404	f
D0384	f
D0364	f
D0344	f
D0324	f
D0304	f
D0284	f
D0264	f
D0244	f
D0224	f
D0204	f
D0184	f
D0164	f
D0144	f
D0124	f
D0104	f
D0084	f
D0064	f
D0044	f
D0024	f
D0483	f
D0463	f
D0443	f
D0423	f
D0403	f
D0383	f
D0363	f
D0343	f
D0323	f
D0303	f
D0283	f
D0263	f
D0243	f
D0223	f
D0203	f
D0183	f
D0163	f
D0143	f
D0123	f
D0103	f
D0083	f
D0063	f
D0043	f
D0023	f
D0481	t
D0461	t
D0441	t
D0421	t
D0401	t
D0381	t
D0361	t
D0341	t
D0321	t
D0301	t
D0281	t
D0261	t
D0241	t
D0221	t
D0201	t
D0181	t
D0161	t
D0141	t
D0121	t
D0101	t
D0081	t
D0061	t
D0041	t
D0021	t
D0480	t
D0460	t
D0440	t
D0420	t
D0400	t
D0380	t
D0360	t
D0340	t
D0320	t
D0300	t
D0280	t
D0260	t
D0240	t
D0220	t
D0200	t
D0180	t
D0160	t
D0140	t
D0120	t
D0100	t
D0080	t
D0060	t
D0040	t
D0020	t
E0475	f
E0455	f
E0435	f
E0415	f
E0395	f
E0375	f
E0355	f
E0335	f
E0315	f
E0295	f
E0275	f
E0255	f
E0235	f
E0215	f
E0195	f
E0175	f
E0155	f
E0135	f
E0115	f
E0095	f
E0075	f
E0055	f
E0035	f
E0015	f
E0474	f
E0454	f
E0434	f
E0414	f
E0394	f
E0374	f
E0354	f
E0334	f
E0314	f
E0294	f
E0274	f
E0254	f
E0234	f
E0214	f
E0194	f
E0174	f
E0154	f
E0134	f
E0114	f
E0094	f
E0074	f
E0054	f
E0034	f
E0014	f
E0473	f
E0453	f
E0433	f
E0413	f
E0393	f
E0373	f
E0353	f
E0333	f
E0313	f
E0293	f
E0273	f
E0253	f
E0233	f
E0213	f
E0193	f
E0173	f
E0153	f
E0133	f
E0113	f
E0093	f
E0073	f
E0053	f
E0033	f
E0013	f
E0471	t
E0451	t
E0431	t
E0411	t
E0391	t
E0371	t
E0351	t
E0331	t
E0311	t
E0291	t
E0271	t
E0251	t
E0231	t
E0211	t
E0191	t
E0171	t
E0151	t
E0131	t
E0111	t
E0091	t
E0071	t
E0051	t
E0031	t
E0011	t
E0470	t
E0450	t
E0430	t
E0410	t
E0390	t
E0370	t
E0350	t
E0330	t
E0310	t
E0290	t
E0270	t
E0250	t
E0230	t
E0210	t
E0190	t
E0170	t
E0150	t
E0130	t
E0110	t
E0090	t
E0070	t
E0050	t
E0030	t
E0010	t
E0485	f
E0465	f
E0445	f
E0425	f
E0405	f
E0385	f
E0365	f
E0345	f
E0325	f
E0305	f
E0285	f
E0265	f
E0245	f
E0225	f
E0205	f
E0185	f
E0165	f
E0145	f
E0125	f
E0105	f
E0085	f
E0065	f
E0045	f
E0025	f
E0484	f
E0464	f
E0444	f
E0424	f
E0404	f
E0384	f
E0364	f
E0344	f
E0324	f
E0304	f
E0284	f
E0264	f
E0244	f
E0224	f
E0204	f
E0184	f
E0164	f
E0144	f
E0124	f
E0104	f
E0084	f
E0064	f
E0044	f
E0024	f
E0483	f
E0463	f
E0443	f
E0423	f
E0403	f
E0383	f
E0363	f
E0343	f
E0323	f
E0303	f
E0283	f
E0263	f
E0243	f
E0223	f
E0203	f
E0183	f
E0163	f
E0143	f
E0123	f
E0103	f
E0083	f
E0063	f
E0043	f
E0023	f
E0481	t
E0461	t
E0441	t
E0421	t
E0401	t
E0381	t
E0361	t
E0341	t
E0321	t
E0301	t
E0281	t
E0261	t
E0241	t
E0221	t
E0201	t
E0181	t
E0161	t
E0141	t
E0121	t
E0101	t
E0081	t
E0061	t
E0041	t
E0021	t
E0480	t
E0460	t
E0440	t
E0420	t
E0400	t
E0380	t
E0360	t
E0340	t
E0320	t
E0300	t
E0280	t
E0260	t
E0240	t
E0220	t
E0200	t
E0180	t
E0160	t
E0140	t
E0120	t
E0100	t
E0080	t
E0060	t
E0040	t
E0020	t
F0475	f
F0455	f
F0435	f
F0415	f
F0395	f
F0375	f
F0355	f
F0335	f
F0315	f
F0295	f
F0275	f
F0255	f
F0235	f
F0215	f
F0195	f
F0175	f
F0155	f
F0135	f
F0115	f
F0095	f
F0075	f
F0055	f
F0035	f
F0015	f
F0474	f
F0454	f
F0434	f
F0414	f
F0394	f
F0374	f
F0354	f
F0334	f
F0314	f
F0294	f
F0274	f
F0254	f
F0234	f
F0214	f
F0194	f
F0174	f
F0154	f
F0134	f
F0114	f
F0094	f
F0074	f
F0054	f
F0034	f
F0014	f
F0473	f
F0453	f
F0433	f
F0413	f
F0393	f
F0373	f
F0353	f
F0333	f
F0313	f
F0293	f
F0273	f
F0253	f
F0233	f
F0213	f
F0193	f
F0173	f
F0153	f
F0133	f
F0113	f
F0093	f
F0073	f
F0053	f
F0033	f
F0013	f
F0471	t
F0451	t
F0431	t
F0411	t
F0391	t
F0371	t
F0351	t
F0331	t
F0311	t
F0291	t
F0271	t
F0251	t
F0231	t
F0211	t
F0191	t
F0171	t
F0151	t
F0131	t
F0111	t
F0091	t
F0071	t
F0051	t
F0031	t
F0011	t
F0470	t
F0450	t
F0430	t
F0410	t
F0390	t
F0370	t
F0350	t
F0330	t
F0310	t
F0290	t
F0270	t
F0250	t
F0230	t
F0210	t
F0190	t
F0170	t
F0150	t
F0130	t
F0110	t
F0090	t
F0070	t
F0050	t
F0030	t
F0010	t
F0485	f
F0465	f
F0445	f
F0425	f
F0405	f
F0385	f
F0365	f
F0345	f
F0325	f
F0305	f
F0285	f
F0265	f
F0245	f
F0225	f
F0205	f
F0185	f
F0165	f
F0145	f
F0125	f
F0105	f
F0085	f
F0065	f
F0045	f
F0025	f
F0484	f
F0464	f
F0444	f
F0424	f
F0404	f
F0384	f
F0364	f
F0344	f
F0324	f
F0304	f
F0284	f
F0264	f
F0244	f
F0224	f
F0204	f
F0184	f
F0164	f
F0144	f
F0124	f
F0104	f
F0084	f
F0064	f
F0044	f
F0024	f
F0483	f
F0463	f
F0443	f
F0423	f
F0403	f
F0383	f
F0363	f
F0343	f
F0323	f
F0303	f
F0283	f
F0263	f
F0243	f
F0223	f
F0203	f
F0183	f
F0163	f
F0143	f
F0123	f
F0103	f
F0083	f
F0063	f
F0043	f
F0023	f
F0481	t
F0461	t
F0441	t
F0421	t
F0401	t
F0381	t
F0361	t
F0341	t
F0321	t
F0301	t
F0281	t
F0261	t
F0241	t
F0221	t
F0201	t
F0181	t
F0161	t
F0141	t
F0121	t
F0101	t
F0081	t
F0061	t
F0041	t
F0021	t
F0480	t
F0460	t
F0440	t
F0420	t
F0400	t
F0380	t
F0360	t
F0340	t
F0320	t
F0300	t
F0280	t
F0260	t
F0240	t
F0220	t
F0200	t
F0180	t
F0160	t
F0140	t
F0120	t
F0100	t
F0080	t
F0060	t
F0040	t
F0020	t
\.


--
-- TOC entry 4935 (class 0 OID 27280)
-- Dependencies: 227
-- Data for Name: stored_on_pallet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stored_on_pallet (id, pnr, id_product, quantity) FROM stdin;
\.


--
-- TOC entry 4937 (class 0 OID 27285)
-- Dependencies: 229
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, password, admin) FROM stdin;
1	admin	admin	t
\.


--
-- TOC entry 4946 (class 0 OID 0)
-- Dependencies: 216
-- Name: customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customer_id_seq', 1, true);


--
-- TOC entry 4947 (class 0 OID 0)
-- Dependencies: 218
-- Name: customer_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customer_reservation_id_seq', 1, false);


--
-- TOC entry 4948 (class 0 OID 0)
-- Dependencies: 220
-- Name: history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.history_id_seq', 1, false);


--
-- TOC entry 4949 (class 0 OID 0)
-- Dependencies: 222
-- Name: material_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.material_id_seq', 1, false);


--
-- TOC entry 4950 (class 0 OID 0)
-- Dependencies: 225
-- Name: pallet_on_position_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pallet_on_position_id_seq', 1, false);


--
-- TOC entry 4951 (class 0 OID 0)
-- Dependencies: 228
-- Name: stored_on_position_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.stored_on_position_id_seq', 1, false);


--
-- TOC entry 4952 (class 0 OID 0)
-- Dependencies: 230
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 1, true);


--
-- TOC entry 4733 (class 2606 OID 27290)
-- Name: customer customer_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_name_key UNIQUE (name);


--
-- TOC entry 4735 (class 2606 OID 27292)
-- Name: customer customer_name_key1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_name_key1 UNIQUE (name);


--
-- TOC entry 4737 (class 2606 OID 27294)
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- TOC entry 4739 (class 2606 OID 27296)
-- Name: customer_reservation customer_reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_pkey PRIMARY KEY (id);


--
-- TOC entry 4746 (class 2606 OID 27298)
-- Name: history history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_pk PRIMARY KEY (id);


--
-- TOC entry 4748 (class 2606 OID 27300)
-- Name: material material_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_name_key UNIQUE (name);


--
-- TOC entry 4750 (class 2606 OID 27302)
-- Name: material material_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (id);


--
-- TOC entry 4757 (class 2606 OID 27304)
-- Name: pallet_on_position pallet_on_position_id_pallet_id_position_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT pallet_on_position_id_pallet_id_position_key UNIQUE (id_pallet, id_position);


--
-- TOC entry 4753 (class 2606 OID 27306)
-- Name: pallet pallete_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_pkey PRIMARY KEY (pnr);


--
-- TOC entry 4759 (class 2606 OID 27308)
-- Name: pallet_on_position pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT pk PRIMARY KEY (id);


--
-- TOC entry 4761 (class 2606 OID 27310)
-- Name: position position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (name);


--
-- TOC entry 4765 (class 2606 OID 27312)
-- Name: stored_on_pallet stored_on_position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pkey PRIMARY KEY (id);


--
-- TOC entry 4767 (class 2606 OID 27314)
-- Name: stored_on_pallet stored_on_position_pnr_id_product_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_id_product_key UNIQUE (pnr, id_product);


--
-- TOC entry 4769 (class 2606 OID 27316)
-- Name: users user_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_name_key UNIQUE (name);


--
-- TOC entry 4771 (class 2606 OID 27318)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 4754 (class 1259 OID 27319)
-- Name: fki_fk_pallet; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fk_pallet ON public.pallet_on_position USING btree (id_pallet);


--
-- TOC entry 4755 (class 1259 OID 27320)
-- Name: fki_fk_position; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fk_position ON public.pallet_on_position USING btree (id_position);


--
-- TOC entry 4740 (class 1259 OID 27321)
-- Name: fki_foreign_key_position; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_foreign_key_position ON public.customer_reservation USING btree (id_position);


--
-- TOC entry 4741 (class 1259 OID 27322)
-- Name: fki_id_customer; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_id_customer ON public.customer_reservation USING btree (id_customer);


--
-- TOC entry 4742 (class 1259 OID 27323)
-- Name: foreign_key_customer; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX foreign_key_customer ON public.customer_reservation USING btree (id_customer);


--
-- TOC entry 4744 (class 1259 OID 27324)
-- Name: foreign_key_history; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX foreign_key_history ON public.history USING btree (id_customer);


--
-- TOC entry 4762 (class 1259 OID 27325)
-- Name: foreign_key_material; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX foreign_key_material ON public.stored_on_pallet USING btree (id_product);


--
-- TOC entry 4763 (class 1259 OID 27326)
-- Name: foreign_key_pnr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX foreign_key_pnr ON public.stored_on_pallet USING btree (pnr);


--
-- TOC entry 4743 (class 1259 OID 27327)
-- Name: foreign_key_position; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX foreign_key_position ON public.customer_reservation USING btree (id_position);


--
-- TOC entry 4751 (class 1259 OID 27328)
-- Name: foreign_key_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX foreign_key_user ON public.pallet USING btree (id_user);


--
-- TOC entry 4772 (class 2606 OID 27329)
-- Name: customer_reservation customer_reservation_id_position_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) ON DELETE CASCADE;


--
-- TOC entry 4776 (class 2606 OID 27334)
-- Name: pallet_on_position fk_pallet; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_pallet FOREIGN KEY (id_pallet) REFERENCES public.pallet(pnr) ON DELETE CASCADE;


--
-- TOC entry 4777 (class 2606 OID 27339)
-- Name: pallet_on_position fk_position; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_position FOREIGN KEY (id_position) REFERENCES public."position"(name) ON DELETE CASCADE;


--
-- TOC entry 4773 (class 2606 OID 27344)
-- Name: customer_reservation id_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT id_customer FOREIGN KEY (id_customer) REFERENCES public.customer(id) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4774 (class 2606 OID 27349)
-- Name: history id_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.history
    ADD CONSTRAINT id_customer FOREIGN KEY (id_customer) REFERENCES public.customer(id) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4775 (class 2606 OID 27354)
-- Name: pallet pallete_id_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- TOC entry 4778 (class 2606 OID 27359)
-- Name: stored_on_pallet stored_on_position_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;


--
-- TOC entry 4779 (class 2606 OID 27364)
-- Name: stored_on_pallet stored_on_position_pnr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_fkey FOREIGN KEY (pnr) REFERENCES public.pallet(pnr) ON DELETE CASCADE;


-- Completed on 2024-02-11 22:31:44

--
-- PostgreSQL database dump complete
--

