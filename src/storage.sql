PGDMP      +            
    {            storage    16.0    16.0 :    :           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            ;           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            <           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            =           1262    16486    storage    DATABASE     |   CREATE DATABASE storage WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
    DROP DATABASE storage;
                postgres    false            �            1259    16558    customer    TABLE     R   CREATE TABLE public.customer (
    id integer NOT NULL,
    name text NOT NULL
);
    DROP TABLE public.customer;
       public         heap    postgres    false            �            1259    16602    customer_id_seq    SEQUENCE     �   ALTER TABLE public.customer ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    222            �            1259    16567    customer_reservation    TABLE     �   CREATE TABLE public.customer_reservation (
    id integer NOT NULL,
    id_customer integer NOT NULL,
    reserved_from date NOT NULL,
    reserved_until date,
    id_position text NOT NULL
);
 (   DROP TABLE public.customer_reservation;
       public         heap    postgres    false            �            1259    16611    customer_reservation_id_seq    SEQUENCE     �   ALTER TABLE public.customer_reservation ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customer_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    223            �            1259    16588    history    TABLE       CREATE TABLE public.history (
    id integer NOT NULL,
    id_customer integer NOT NULL,
    "time" time without time zone NOT NULL,
    date date NOT NULL,
    truck_income boolean DEFAULT true NOT NULL,
    number_of_pallets integer NOT NULL,
    truck_number integer NOT NULL
);
    DROP TABLE public.history;
       public         heap    postgres    false            >           0    0    COLUMN history.truck_income    COMMENT     k   COMMENT ON COLUMN public.history.truck_income IS 'poznamka, ci kamion prisiel s paletami alebo odišiel
';
          public          postgres    false    224            ?           0    0    COLUMN history.truck_number    COMMENT     C   COMMENT ON COLUMN public.history.truck_number IS 'číslo točky';
          public          postgres    false    224            �            1259    16527    material    TABLE     R   CREATE TABLE public.material (
    id integer NOT NULL,
    name text NOT NULL
);
    DROP TABLE public.material;
       public         heap    postgres    false            �            1259    16526    material_id_seq    SEQUENCE     �   ALTER TABLE public.material ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.material_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    221            �            1259    16510    pallete    TABLE     -  CREATE TABLE public.pallete (
    pnr text NOT NULL,
    weight integer DEFAULT 500 NOT NULL,
    date_income date NOT NULL,
    is_damaged boolean DEFAULT false NOT NULL,
    id_user integer NOT NULL,
    type text DEFAULT 'europaleta'::text NOT NULL,
    note text,
    id_position text NOT NULL
);
    DROP TABLE public.pallete;
       public         heap    postgres    false            �            1259    16496    position    TABLE     |   CREATE TABLE public."position" (
    name text NOT NULL,
    is_high boolean GENERATED ALWAYS AS (false) STORED NOT NULL
);
    DROP TABLE public."position";
       public         heap    postgres    false            �            1259    16520    stored_on_position    TABLE     �   CREATE TABLE public.stored_on_position (
    id integer NOT NULL,
    pnr text NOT NULL,
    id_product integer NOT NULL,
    quantity integer DEFAULT 1 NOT NULL
);
 &   DROP TABLE public.stored_on_position;
       public         heap    postgres    false            �            1259    16622    stored_on_position_id_seq    SEQUENCE     �   ALTER TABLE public.stored_on_position ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.stored_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    219            �            1259    16488    user    TABLE     v   CREATE TABLE public."user" (
    id integer NOT NULL,
    name text NOT NULL,
    password text,
    admin boolean
);
    DROP TABLE public."user";
       public         heap    postgres    false            �            1259    16487    user_id_seq    SEQUENCE     �   ALTER TABLE public."user" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            2          0    16558    customer 
   TABLE DATA           ,   COPY public.customer (id, name) FROM stdin;
    public          postgres    false    222   aD       3          0    16567    customer_reservation 
   TABLE DATA           k   COPY public.customer_reservation (id, id_customer, reserved_from, reserved_until, id_position) FROM stdin;
    public          postgres    false    223   �D       4          0    16588    history 
   TABLE DATA           o   COPY public.history (id, id_customer, "time", date, truck_income, number_of_pallets, truck_number) FROM stdin;
    public          postgres    false    224   �D       1          0    16527    material 
   TABLE DATA           ,   COPY public.material (id, name) FROM stdin;
    public          postgres    false    221   �D       .          0    16510    pallete 
   TABLE DATA           i   COPY public.pallete (pnr, weight, date_income, is_damaged, id_user, type, note, id_position) FROM stdin;
    public          postgres    false    218   8E       -          0    16496    position 
   TABLE DATA           *   COPY public."position" (name) FROM stdin;
    public          postgres    false    217   �E       /          0    16520    stored_on_position 
   TABLE DATA           K   COPY public.stored_on_position (id, pnr, id_product, quantity) FROM stdin;
    public          postgres    false    219   �E       ,          0    16488    user 
   TABLE DATA           ;   COPY public."user" (id, name, password, admin) FROM stdin;
    public          postgres    false    216   F       @           0    0    customer_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.customer_id_seq', 5, true);
          public          postgres    false    225            A           0    0    customer_reservation_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.customer_reservation_id_seq', 5, true);
          public          postgres    false    226            B           0    0    material_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.material_id_seq', 4, true);
          public          postgres    false    220            C           0    0    stored_on_position_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.stored_on_position_id_seq', 3, true);
          public          postgres    false    227            D           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 3, true);
          public          postgres    false    215            �           2606    16566    customer customer_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_name_key;
       public            postgres    false    222            �           2606    16624    customer customer_name_key1 
   CONSTRAINT     V   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_name_key1 UNIQUE (name);
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_name_key1;
       public            postgres    false    222            �           2606    16564    customer customer_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public            postgres    false    222            �           2606    16613 9   customer_reservation customer_reservation_id_position_key 
   CONSTRAINT     {   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_key UNIQUE (id_position);
 c   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_position_key;
       public            postgres    false    223            �           2606    16593    history history_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_pkey PRIMARY KEY (id_customer);
 >   ALTER TABLE ONLY public.history DROP CONSTRAINT history_pkey;
       public            postgres    false    224            �           2606    16615    material material_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.material DROP CONSTRAINT material_name_key;
       public            postgres    false    221            �           2606    16533    material material_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.material DROP CONSTRAINT material_pkey;
       public            postgres    false    221            }           2606    16519    pallete pallete_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.pallete
    ADD CONSTRAINT pallete_pkey PRIMARY KEY (pnr);
 >   ALTER TABLE ONLY public.pallete DROP CONSTRAINT pallete_pkey;
       public            postgres    false    218            y           2606    16601    position position_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (name);
 B   ALTER TABLE ONLY public."position" DROP CONSTRAINT position_pkey;
       public            postgres    false    217            �           2606    16587 *   stored_on_position stored_on_position_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.stored_on_position
    ADD CONSTRAINT stored_on_position_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.stored_on_position DROP CONSTRAINT stored_on_position_pkey;
       public            postgres    false    219            �           2606    16585 8   stored_on_position stored_on_position_pnr_id_product_key 
   CONSTRAINT     ~   ALTER TABLE ONLY public.stored_on_position
    ADD CONSTRAINT stored_on_position_pnr_id_product_key UNIQUE (pnr, id_product);
 b   ALTER TABLE ONLY public.stored_on_position DROP CONSTRAINT stored_on_position_pnr_id_product_key;
       public            postgres    false    219    219            w           2606    16492    user user_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_pkey;
       public            postgres    false    216            z           1259    16621    fg_pallete_position    INDEX     N   CREATE INDEX fg_pallete_position ON public.pallete USING btree (id_position);
 '   DROP INDEX public.fg_pallete_position;
       public            postgres    false    218            �           1259    16583    foreign_key_customer    INDEX     \   CREATE INDEX foreign_key_customer ON public.customer_reservation USING btree (id_customer);
 (   DROP INDEX public.foreign_key_customer;
       public            postgres    false    223            �           1259    16599    foreign_key_history    INDEX     N   CREATE INDEX foreign_key_history ON public.history USING btree (id_customer);
 '   DROP INDEX public.foreign_key_history;
       public            postgres    false    224            ~           1259    16557    foreign_key_material    INDEX     Y   CREATE INDEX foreign_key_material ON public.stored_on_position USING btree (id_product);
 (   DROP INDEX public.foreign_key_material;
       public            postgres    false    219                       1259    16551    foreign_key_pnr    INDEX     M   CREATE INDEX foreign_key_pnr ON public.stored_on_position USING btree (pnr);
 #   DROP INDEX public.foreign_key_pnr;
       public            postgres    false    219            �           1259    16610    foreign_key_position    INDEX     \   CREATE INDEX foreign_key_position ON public.customer_reservation USING btree (id_position);
 (   DROP INDEX public.foreign_key_position;
       public            postgres    false    223            {           1259    16539    foreign_key_user    INDEX     G   CREATE INDEX foreign_key_user ON public.pallete USING btree (id_user);
 $   DROP INDEX public.foreign_key_user;
       public            postgres    false    218            �           2606    16578 :   customer_reservation customer_reservation_id_customer_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES public.customer(id) NOT VALID;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_customer_fkey;
       public          postgres    false    223    222    4749            �           2606    16605 :   customer_reservation customer_reservation_id_position_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_position_fkey;
       public          postgres    false    4729    217    223            �           2606    16594     history history_id_customer_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES public.customer(id) NOT VALID;
 J   ALTER TABLE ONLY public.history DROP CONSTRAINT history_id_customer_fkey;
       public          postgres    false    4749    224    222            �           2606    16616     pallete pallete_id_position_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallete
    ADD CONSTRAINT pallete_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 J   ALTER TABLE ONLY public.pallete DROP CONSTRAINT pallete_id_position_fkey;
       public          postgres    false    217    4729    218            �           2606    16534    pallete pallete_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallete
    ADD CONSTRAINT pallete_id_user_fkey FOREIGN KEY (id_user) REFERENCES public."user"(id) NOT VALID;
 F   ALTER TABLE ONLY public.pallete DROP CONSTRAINT pallete_id_user_fkey;
       public          postgres    false    4727    218    216            �           2606    16552 5   stored_on_position stored_on_position_id_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_position
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;
 _   ALTER TABLE ONLY public.stored_on_position DROP CONSTRAINT stored_on_position_id_product_fkey;
       public          postgres    false    219    221    4743            �           2606    16546 .   stored_on_position stored_on_position_pnr_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_position
    ADD CONSTRAINT stored_on_position_pnr_fkey FOREIGN KEY (pnr) REFERENCES public.pallete(pnr) NOT VALID;
 X   ALTER TABLE ONLY public.stored_on_position DROP CONSTRAINT stored_on_position_pnr_fkey;
       public          postgres    false    4733    219    218            2   "   x�3�
��2���)��2�t������ T��      3   0   x�3�4�4202�54�54�3�8�L9!��X䍸b���� ��o      4      x������ � �      1   8   x�3���/�/R0600�2�r�lc���̼D.ΐ����ĒԢ���=... �<�      .   d   x�300�450�4202�54�5��L�4�L--�/H�I-I�+N-��W�JU(S(�/�LL�.�t4000�200�44�ԝ�YR�Z��_�陗� 5�%F��� �?�      -      x�s4000�r�F`Ҙ+F��� 2�      /      x�3�400�4�4�2��8���=... ,EC      ,   ,   x�3�LL��̃�%\F��%�EP��˘�-�(B�q��qqq ��&     