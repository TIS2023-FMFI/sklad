toc.dat                                                                                             0000600 0004000 0002000 00000013624 14524627670 0014462 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        PGDMP   '        	        
    {            skusobnaDatabaza    16.0    16.0     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false         �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false         �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                     1262    16397    skusobnaDatabaza    DATABASE     �   CREATE DATABASE "skusobnaDatabaza" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
 "   DROP DATABASE "skusobnaDatabaza";
                postgres    false         �            1259    16407    product    TABLE     n   CREATE TABLE public.product (
    id integer NOT NULL,
    name text NOT NULL,
    weight integer NOT NULL
);
    DROP TABLE public.product;
       public         heap    postgres    false         �            1259    16406    product_id_seq    SEQUENCE     �   ALTER TABLE public.product ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    218         �            1259    16444    projekt    TABLE     N   CREATE TABLE public.projekt (
    meno text,
    id integer,
    rola text
);
    DROP TABLE public.projekt;
       public         heap    postgres    false         �            1259    16414    responsibilities    TABLE        CREATE TABLE public.responsibilities (
    iduser integer NOT NULL,
    idproduct integer NOT NULL,
    id integer NOT NULL
);
 $   DROP TABLE public.responsibilities;
       public         heap    postgres    false         �            1259    16440    responsibilities_id_seq    SEQUENCE     �   ALTER TABLE public.responsibilities ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.responsibilities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    219         �            1259    16399    users    TABLE     h   CREATE TABLE public.users (
    id integer NOT NULL,
    name text NOT NULL,
    email text NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false         �            1259    16398    user_id_seq    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 100000
    CACHE 1
);
            public          postgres    false    216         �          0    16407    product 
   TABLE DATA           3   COPY public.product (id, name, weight) FROM stdin;
    public          postgres    false    218       4855.dat �          0    16444    projekt 
   TABLE DATA           1   COPY public.projekt (meno, id, rola) FROM stdin;
    public          postgres    false    221       4858.dat �          0    16414    responsibilities 
   TABLE DATA           A   COPY public.responsibilities (iduser, idproduct, id) FROM stdin;
    public          postgres    false    219       4856.dat �          0    16399    users 
   TABLE DATA           0   COPY public.users (id, name, email) FROM stdin;
    public          postgres    false    216       4853.dat            0    0    product_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.product_id_seq', 4, true);
          public          postgres    false    217                    0    0    responsibilities_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.responsibilities_id_seq', 6, true);
          public          postgres    false    220                    0    0    user_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.user_id_seq', 11, true);
          public          postgres    false    215         b           2606    16411    product product_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public            postgres    false    218         `           2606    16403    users user_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 9   ALTER TABLE ONLY public.users DROP CONSTRAINT user_pkey;
       public            postgres    false    216         ^           1259    16423    fj_01    INDEX     5   CREATE INDEX fj_01 ON public.users USING btree (id);
    DROP INDEX public.fj_01;
       public            postgres    false    216         c           2606    16429 0   responsibilities responsibilities_idProduct_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.responsibilities
    ADD CONSTRAINT "responsibilities_idProduct_fkey" FOREIGN KEY (idproduct) REFERENCES public.product(id) NOT VALID;
 \   ALTER TABLE ONLY public.responsibilities DROP CONSTRAINT "responsibilities_idProduct_fkey";
       public          postgres    false    4706    218    219         d           2606    16424 -   responsibilities responsibilities_idUser_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.responsibilities
    ADD CONSTRAINT "responsibilities_idUser_fkey" FOREIGN KEY (iduser) REFERENCES public.users(id) NOT VALID;
 Y   ALTER TABLE ONLY public.responsibilities DROP CONSTRAINT "responsibilities_idUser_fkey";
       public          postgres    false    216    4704    219                                                                                                                    4855.dat                                                                                            0000600 0004000 0002000 00000000063 14524627670 0014273 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	pero	1
2	motor 3000	500
3	stol	15
4	tehla	5
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                             4858.dat                                                                                            0000600 0004000 0002000 00000000135 14524627670 0014276 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        Ali	4	Nič
Ali	4	Nič
Ali	4	Nič
Ali	4	Nič
Ali	4	Nič
Ali	4	Nič
Ali	4	Nič
Ali	4	Nič
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                   4856.dat                                                                                            0000600 0004000 0002000 00000000051 14524627670 0014271 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	1	1
1	2	2
2	2	3
2	3	4
3	2	5
3	4	6
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       4853.dat                                                                                            0000600 0004000 0002000 00000000121 14524627670 0014264 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	Sasa	snidova@uniba.sk
2	Matej	palider@uniba.sk
3	Patrik	filipiak@uniba.sk
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                               restore.sql                                                                                         0000600 0004000 0002000 00000012534 14524627670 0015406 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        --
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

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

DROP DATABASE "skusobnaDatabaza";
--
-- Name: skusobnaDatabaza; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "skusobnaDatabaza" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';


ALTER DATABASE "skusobnaDatabaza" OWNER TO postgres;

\connect "skusobnaDatabaza"

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
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id integer NOT NULL,
    name text NOT NULL,
    weight integer NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.product ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: projekt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.projekt (
    meno text,
    id integer,
    rola text
);


ALTER TABLE public.projekt OWNER TO postgres;

--
-- Name: responsibilities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.responsibilities (
    iduser integer NOT NULL,
    idproduct integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.responsibilities OWNER TO postgres;

--
-- Name: responsibilities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.responsibilities ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.responsibilities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name text NOT NULL,
    email text NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 100000
    CACHE 1
);


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, name, weight) FROM stdin;
\.
COPY public.product (id, name, weight) FROM '$$PATH$$/4855.dat';

--
-- Data for Name: projekt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.projekt (meno, id, rola) FROM stdin;
\.
COPY public.projekt (meno, id, rola) FROM '$$PATH$$/4858.dat';

--
-- Data for Name: responsibilities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.responsibilities (iduser, idproduct, id) FROM stdin;
\.
COPY public.responsibilities (iduser, idproduct, id) FROM '$$PATH$$/4856.dat';

--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, email) FROM stdin;
\.
COPY public.users (id, name, email) FROM '$$PATH$$/4853.dat';

--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_seq', 4, true);


--
-- Name: responsibilities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.responsibilities_id_seq', 6, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 11, true);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: fj_01; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fj_01 ON public.users USING btree (id);


--
-- Name: responsibilities responsibilities_idProduct_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsibilities
    ADD CONSTRAINT "responsibilities_idProduct_fkey" FOREIGN KEY (idproduct) REFERENCES public.product(id) NOT VALID;


--
-- Name: responsibilities responsibilities_idUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsibilities
    ADD CONSTRAINT "responsibilities_idUser_fkey" FOREIGN KEY (iduser) REFERENCES public.users(id) NOT VALID;


--
-- PostgreSQL database dump complete
--

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    