PGDMP      .                {            storage    16.0    16.0 F    M           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            N           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            O           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            P           1262    16486    storage    DATABASE     |   CREATE DATABASE storage WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
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
    reserved_until date NOT NULL,
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
       public         heap    postgres    false            Q           0    0    COLUMN history.truck_income    COMMENT     k   COMMENT ON COLUMN public.history.truck_income IS 'poznamka, ci kamion prisiel s paletami alebo odišiel
';
          public          postgres    false    224            R           0    0    COLUMN history.truck_number    COMMENT     C   COMMENT ON COLUMN public.history.truck_number IS 'číslo točky';
          public          postgres    false    224            �            1259    24816    history_id_seq    SEQUENCE     �   ALTER TABLE public.history ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    224            �            1259    16527    material    TABLE     R   CREATE TABLE public.material (
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
            public          postgres    false    221            �            1259    16510    pallet    TABLE       CREATE TABLE public.pallet (
    pnr text NOT NULL,
    weight integer DEFAULT 500 NOT NULL,
    date_income date NOT NULL,
    is_damaged boolean DEFAULT false NOT NULL,
    id_user integer NOT NULL,
    type text DEFAULT 'europaleta'::text NOT NULL,
    note text
);
    DROP TABLE public.pallet;
       public         heap    postgres    false            �            1259    16629    pallet_on_position    TABLE     �   CREATE TABLE public.pallet_on_position (
    id integer NOT NULL,
    id_pallet text NOT NULL,
    id_position text NOT NULL
);
 &   DROP TABLE public.pallet_on_position;
       public         heap    postgres    false            �            1259    24815    pallet_on_position_id_seq    SEQUENCE     �   ALTER TABLE public.pallet_on_position ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pallet_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    228            �            1259    16496    position    TABLE     g   CREATE TABLE public."position" (
    name text NOT NULL,
    is_tall boolean DEFAULT false NOT NULL
);
    DROP TABLE public."position";
       public         heap    postgres    false            �            1259    16520    stored_on_pallet    TABLE     �   CREATE TABLE public.stored_on_pallet (
    id integer NOT NULL,
    pnr text NOT NULL,
    id_product integer NOT NULL,
    quantity integer DEFAULT 1 NOT NULL
);
 $   DROP TABLE public.stored_on_pallet;
       public         heap    postgres    false            �            1259    16622    stored_on_position_id_seq    SEQUENCE     �   ALTER TABLE public.stored_on_pallet ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.stored_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    219            �            1259    16488    users    TABLE     u   CREATE TABLE public.users (
    id integer NOT NULL,
    name text NOT NULL,
    password text,
    admin boolean
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    16487    user_id_seq    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            B          0    16558    customer 
   TABLE DATA           ,   COPY public.customer (id, name) FROM stdin;
    public          postgres    false    222   �R       C          0    16567    customer_reservation 
   TABLE DATA           k   COPY public.customer_reservation (id, id_customer, reserved_from, reserved_until, id_position) FROM stdin;
    public          postgres    false    223   �R       D          0    16588    history 
   TABLE DATA           o   COPY public.history (id, id_customer, "time", date, truck_income, number_of_pallets, truck_number) FROM stdin;
    public          postgres    false    224   S       A          0    16527    material 
   TABLE DATA           ,   COPY public.material (id, name) FROM stdin;
    public          postgres    false    221   $S       >          0    16510    pallet 
   TABLE DATA           [   COPY public.pallet (pnr, weight, date_income, is_damaged, id_user, type, note) FROM stdin;
    public          postgres    false    218   qS       H          0    16629    pallet_on_position 
   TABLE DATA           H   COPY public.pallet_on_position (id, id_pallet, id_position) FROM stdin;
    public          postgres    false    228   �S       =          0    16496    position 
   TABLE DATA           3   COPY public."position" (name, is_tall) FROM stdin;
    public          postgres    false    217   T       ?          0    16520    stored_on_pallet 
   TABLE DATA           I   COPY public.stored_on_pallet (id, pnr, id_product, quantity) FROM stdin;
    public          postgres    false    219   MT       <          0    16488    users 
   TABLE DATA           :   COPY public.users (id, name, password, admin) FROM stdin;
    public          postgres    false    216   {T       S           0    0    customer_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.customer_id_seq', 5, true);
          public          postgres    false    225            T           0    0    customer_reservation_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.customer_reservation_id_seq', 5, true);
          public          postgres    false    226            U           0    0    history_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.history_id_seq', 1, false);
          public          postgres    false    230            V           0    0    material_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.material_id_seq', 7, true);
          public          postgres    false    220            W           0    0    pallet_on_position_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.pallet_on_position_id_seq', 3, true);
          public          postgres    false    229            X           0    0    stored_on_position_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.stored_on_position_id_seq', 3, true);
          public          postgres    false    227            Y           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 3, true);
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
       public            postgres    false    223            �           2606    16651 .   customer_reservation customer_reservation_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_pkey;
       public            postgres    false    223            �           2606    16628    history history_pk 
   CONSTRAINT     P   ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_pk PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.history DROP CONSTRAINT history_pk;
       public            postgres    false    224            �           2606    16615    material material_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.material DROP CONSTRAINT material_name_key;
       public            postgres    false    221            �           2606    16533    material material_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.material DROP CONSTRAINT material_pkey;
       public            postgres    false    221            �           2606    16653 ?   pallet_on_position pallet_on_position_id_pallet_id_position_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT pallet_on_position_id_pallet_id_position_key UNIQUE (id_pallet, id_position);
 i   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT pallet_on_position_id_pallet_id_position_key;
       public            postgres    false    228    228            �           2606    16519    pallet pallete_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_pkey PRIMARY KEY (pnr);
 =   ALTER TABLE ONLY public.pallet DROP CONSTRAINT pallete_pkey;
       public            postgres    false    218            �           2606    16649    pallet_on_position pk 
   CONSTRAINT     S   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT pk PRIMARY KEY (id);
 ?   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT pk;
       public            postgres    false    228            �           2606    16601    position position_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (name);
 B   ALTER TABLE ONLY public."position" DROP CONSTRAINT position_pkey;
       public            postgres    false    217            �           2606    16587 (   stored_on_pallet stored_on_position_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pkey;
       public            postgres    false    219            �           2606    16585 6   stored_on_pallet stored_on_position_pnr_id_product_key 
   CONSTRAINT     |   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_id_product_key UNIQUE (pnr, id_product);
 `   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pnr_id_product_key;
       public            postgres    false    219    219            }           2606    16626    users user_name_key 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_name_key UNIQUE (name);
 =   ALTER TABLE ONLY public.users DROP CONSTRAINT user_name_key;
       public            postgres    false    216                       2606    16492    users user_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 9   ALTER TABLE ONLY public.users DROP CONSTRAINT user_pkey;
       public            postgres    false    216            �           1259    16647    fki_fk_pallet    INDEX     Q   CREATE INDEX fki_fk_pallet ON public.pallet_on_position USING btree (id_pallet);
 !   DROP INDEX public.fki_fk_pallet;
       public            postgres    false    228            �           1259    16641    fki_fk_position    INDEX     U   CREATE INDEX fki_fk_position ON public.pallet_on_position USING btree (id_position);
 #   DROP INDEX public.fki_fk_position;
       public            postgres    false    228            �           1259    16583    foreign_key_customer    INDEX     \   CREATE INDEX foreign_key_customer ON public.customer_reservation USING btree (id_customer);
 (   DROP INDEX public.foreign_key_customer;
       public            postgres    false    223            �           1259    16599    foreign_key_history    INDEX     N   CREATE INDEX foreign_key_history ON public.history USING btree (id_customer);
 '   DROP INDEX public.foreign_key_history;
       public            postgres    false    224            �           1259    16557    foreign_key_material    INDEX     W   CREATE INDEX foreign_key_material ON public.stored_on_pallet USING btree (id_product);
 (   DROP INDEX public.foreign_key_material;
       public            postgres    false    219            �           1259    16551    foreign_key_pnr    INDEX     K   CREATE INDEX foreign_key_pnr ON public.stored_on_pallet USING btree (pnr);
 #   DROP INDEX public.foreign_key_pnr;
       public            postgres    false    219            �           1259    16610    foreign_key_position    INDEX     \   CREATE INDEX foreign_key_position ON public.customer_reservation USING btree (id_position);
 (   DROP INDEX public.foreign_key_position;
       public            postgres    false    223            �           1259    16539    foreign_key_user    INDEX     F   CREATE INDEX foreign_key_user ON public.pallet USING btree (id_user);
 $   DROP INDEX public.foreign_key_user;
       public            postgres    false    218            �           2606    16578 :   customer_reservation customer_reservation_id_customer_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES public.customer(id) NOT VALID;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_customer_fkey;
       public          postgres    false    222    223    4756            �           2606    16605 :   customer_reservation customer_reservation_id_position_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_position_fkey;
       public          postgres    false    223    4737    217            �           2606    16642    pallet_on_position fk_pallet    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_pallet FOREIGN KEY (id_pallet) REFERENCES public.pallet(pnr) NOT VALID;
 F   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT fk_pallet;
       public          postgres    false    218    4740    228            �           2606    16636    pallet_on_position fk_position    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_position FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 H   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT fk_position;
       public          postgres    false    217    4737    228            �           2606    16594     history history_id_customer_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES public.customer(id) NOT VALID;
 J   ALTER TABLE ONLY public.history DROP CONSTRAINT history_id_customer_fkey;
       public          postgres    false    224    4756    222            �           2606    16534    pallet pallete_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id) NOT VALID;
 E   ALTER TABLE ONLY public.pallet DROP CONSTRAINT pallete_id_user_fkey;
       public          postgres    false    218    4735    216            �           2606    16552 3   stored_on_pallet stored_on_position_id_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;
 ]   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_id_product_fkey;
       public          postgres    false    219    4750    221            �           2606    16546 ,   stored_on_pallet stored_on_position_pnr_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_fkey FOREIGN KEY (pnr) REFERENCES public.pallet(pnr) NOT VALID;
 V   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pnr_fkey;
       public          postgres    false    218    219    4740            B   "   x�3�
��2���)��2�t������ T��      C   0   x�3�4�4202�54�54�3�8�L9!��X䍸b���� ��o      D      x������ � �      A   =   x�3���/�/R0600�2�r�lc���̼D.ΐ����ĒԢ��.3T~)W� ߒ�      >   l   x���;@@@�S�VfF��Q�4+����Z��"
�F�>DIj�-�Ё����n���4Nތj��0�vڑH����C�B�8�Pn𝈕Ϥ.Q�?T� ��\-�      H      x�3�4200�t�\F�W� O��      =   #   x�s4000�L�r�FP�Lp�p��qqq ���      ?      x�3�400�4�4�2��8���=... ,EC      <   ,   x�3�LL��̃�%\F��%�EP��˘�-�(B�q��qqq ��&     