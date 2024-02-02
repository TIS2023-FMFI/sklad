PGDMP                      |            storage    16.0    16.0 M    R           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            S           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            T           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            U           1262    16486    storage    DATABASE     |   CREATE DATABASE storage WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
    DROP DATABASE storage;
                postgres    false            �            1259    16558    customer    TABLE     �   CREATE TABLE public.customer (
    id integer NOT NULL,
    name text NOT NULL,
    address text NOT NULL,
    city text NOT NULL,
    postal_code text NOT NULL,
    ico_value text,
    dic_value text
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
       public         heap    postgres    false            V           0    0    COLUMN history.truck_income    COMMENT     k   COMMENT ON COLUMN public.history.truck_income IS 'poznamka, ci kamion prisiel s paletami alebo odišiel
';
          public          postgres    false    224            W           0    0    COLUMN history.truck_number    COMMENT     C   COMMENT ON COLUMN public.history.truck_number IS 'číslo točky';
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
            public          postgres    false    221            �            1259    16510    pallet    TABLE     4  CREATE TABLE public.pallet (
    pnr text NOT NULL,
    date_income date NOT NULL,
    is_damaged boolean DEFAULT false NOT NULL,
    id_user integer NOT NULL,
    type text DEFAULT 'europaleta'::text NOT NULL,
    note text,
    weight double precision NOT NULL,
    number_of_positions integer NOT NULL
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
            public          postgres    false    216            G          0    16558    customer 
   TABLE DATA           ^   COPY public.customer (id, name, address, city, postal_code, ico_value, dic_value) FROM stdin;
    public          postgres    false    222   TZ       H          0    16567    customer_reservation 
   TABLE DATA           k   COPY public.customer_reservation (id, id_customer, reserved_from, reserved_until, id_position) FROM stdin;
    public          postgres    false    223   �Z       I          0    16588    history 
   TABLE DATA           o   COPY public.history (id, id_customer, "time", date, truck_income, number_of_pallets, truck_number) FROM stdin;
    public          postgres    false    224   �Z       F          0    16527    material 
   TABLE DATA           ,   COPY public.material (id, name) FROM stdin;
    public          postgres    false    221   n[       C          0    16510    pallet 
   TABLE DATA           p   COPY public.pallet (pnr, date_income, is_damaged, id_user, type, note, weight, number_of_positions) FROM stdin;
    public          postgres    false    218   �[       M          0    16629    pallet_on_position 
   TABLE DATA           H   COPY public.pallet_on_position (id, id_pallet, id_position) FROM stdin;
    public          postgres    false    228   N\       B          0    16496    position 
   TABLE DATA           3   COPY public."position" (name, is_tall) FROM stdin;
    public          postgres    false    217   k\       D          0    16520    stored_on_pallet 
   TABLE DATA           I   COPY public.stored_on_pallet (id, pnr, id_product, quantity) FROM stdin;
    public          postgres    false    219   �g       A          0    16488    users 
   TABLE DATA           :   COPY public.users (id, name, password, admin) FROM stdin;
    public          postgres    false    216   �g       X           0    0    customer_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.customer_id_seq', 48, true);
          public          postgres    false    225            Y           0    0    customer_reservation_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.customer_reservation_id_seq', 9600, true);
          public          postgres    false    226            Z           0    0    history_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.history_id_seq', 13, true);
          public          postgres    false    230            [           0    0    material_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.material_id_seq', 19, true);
          public          postgres    false    220            \           0    0    pallet_on_position_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.pallet_on_position_id_seq', 46, true);
          public          postgres    false    229            ]           0    0    stored_on_position_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.stored_on_position_id_seq', 22, true);
          public          postgres    false    227            ^           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 3, true);
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
       public            postgres    false    222            �           2606    16651 .   customer_reservation customer_reservation_pkey 
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
       public            postgres    false    219    219            |           2606    16626    users user_name_key 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_name_key UNIQUE (name);
 =   ALTER TABLE ONLY public.users DROP CONSTRAINT user_name_key;
       public            postgres    false    216            ~           2606    16492    users user_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 9   ALTER TABLE ONLY public.users DROP CONSTRAINT user_pkey;
       public            postgres    false    216            �           1259    24897    fki_FK_idPallet    INDEX     M   CREATE INDEX "fki_FK_idPallet" ON public.stored_on_pallet USING btree (pnr);
 %   DROP INDEX public."fki_FK_idPallet";
       public            postgres    false    219            �           1259    24903    fki_FK_idPosition    INDEX     Y   CREATE INDEX "fki_FK_idPosition" ON public.pallet_on_position USING btree (id_position);
 '   DROP INDEX public."fki_FK_idPosition";
       public            postgres    false    228            �           1259    24879    fki_FK_idUser    INDEX     E   CREATE INDEX "fki_FK_idUser" ON public.pallet USING btree (id_user);
 #   DROP INDEX public."fki_FK_idUser";
       public            postgres    false    218            �           1259    16647    fki_fk_pallet    INDEX     Q   CREATE INDEX fki_fk_pallet ON public.pallet_on_position USING btree (id_pallet);
 !   DROP INDEX public.fki_fk_pallet;
       public            postgres    false    228            �           1259    16641    fki_fk_position    INDEX     U   CREATE INDEX fki_fk_position ON public.pallet_on_position USING btree (id_position);
 #   DROP INDEX public.fki_fk_position;
       public            postgres    false    228            �           1259    24838    fki_foreign_key_position    INDEX     `   CREATE INDEX fki_foreign_key_position ON public.customer_reservation USING btree (id_position);
 ,   DROP INDEX public.fki_foreign_key_position;
       public            postgres    false    223            �           1259    24891    fki_i    INDEX     I   CREATE INDEX fki_i ON public.pallet_on_position USING btree (id_pallet);
    DROP INDEX public.fki_i;
       public            postgres    false    228            �           1259    24868    fki_id_customer    INDEX     W   CREATE INDEX fki_id_customer ON public.customer_reservation USING btree (id_customer);
 #   DROP INDEX public.fki_id_customer;
       public            postgres    false    223            �           1259    24885    fki_id_user    INDEX     A   CREATE INDEX fki_id_user ON public.pallet USING btree (id_user);
    DROP INDEX public.fki_id_user;
       public            postgres    false    218            �           1259    16583    foreign_key_customer    INDEX     \   CREATE INDEX foreign_key_customer ON public.customer_reservation USING btree (id_customer);
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
       public            postgres    false    218            �           2606    24892    stored_on_pallet FK_idPallet    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT "FK_idPallet" FOREIGN KEY (pnr) REFERENCES public.pallet(pnr) ON DELETE CASCADE NOT VALID;
 H   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT "FK_idPallet";
       public          postgres    false    219    4741    218            �           2606    24904    pallet_on_position FK_idPallet    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT "FK_idPallet" FOREIGN KEY (id_pallet) REFERENCES public.pallet(pnr) ON DELETE CASCADE NOT VALID;
 J   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT "FK_idPallet";
       public          postgres    false    218    228    4741            �           2606    24898     pallet_on_position FK_idPosition    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT "FK_idPosition" FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 L   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT "FK_idPosition";
       public          postgres    false    228    4736    217            �           2606    16605 :   customer_reservation customer_reservation_id_position_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_position_fkey;
       public          postgres    false    217    4736    223            �           2606    24833 )   customer_reservation foreign_key_position    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT foreign_key_position FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 S   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT foreign_key_position;
       public          postgres    false    4736    217    223            �           2606    24863     customer_reservation id_customer    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT id_customer FOREIGN KEY (id_customer) REFERENCES public.customer(id) ON DELETE CASCADE NOT VALID;
 J   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT id_customer;
       public          postgres    false    222    223    4758            �           2606    24869    history id_customer    FK CONSTRAINT     �   ALTER TABLE ONLY public.history
    ADD CONSTRAINT id_customer FOREIGN KEY (id_customer) REFERENCES public.customer(id) ON DELETE CASCADE NOT VALID;
 =   ALTER TABLE ONLY public.history DROP CONSTRAINT id_customer;
       public          postgres    false    4758    224    222            �           2606    24909    pallet id_user    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT id_user FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE CASCADE NOT VALID;
 8   ALTER TABLE ONLY public.pallet DROP CONSTRAINT id_user;
       public          postgres    false    216    218    4734            �           2606    16552 3   stored_on_pallet stored_on_position_id_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;
 ]   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_id_product_fkey;
       public          postgres    false    221    219    4752            G   !   x�3��t�LCN.sNG�D�D �b���� Z�1      H   B   x�}ʱ� �:��K$���s6V���+լvF-�B�v�N�"��'�����{��4b �e�      I   �   x�m��C!Dѵ��66��%�T���V��h�B�Il����2kf�7)Iҋ��;�|Y�EY���2`���-.:�|�=�!�s�ݹ-6ם{�|���X�?ą�g�P	��q����@O�n��͟����F      F      x�Uȱ�0����� q7����8��rc�4E����֤�����[Ha�c�����w�)[<�����iw�����=6�� O1X)��MI�(ui���]v�Q�RN�(��f���Δ�b#e����H��-{      C   A   x�3�4202�50�50�L�4�L--�/H�I-I���450�4�22 RuF@�`u�uPU1z\\\ �W�      M      x������ � �      B   Q  x�5�A�[���q���d����������i��F�c�=�O��'���_���O쇻�����-nq�+���+.��K.���.�������rs{~�[��W\q�W\r�%�\r�\p����������.���-���+���K.��.����_����fy~�[��W\q�W\r�%�\r�\p������r3=��-nq��|��'�\r�\p����|�r3<��-nq��˟��������K.��.����<�w!|�/��n�+.��K.���.>�n���w�r��v��K.��.������2|w/��n�+.��K.���.>�n���w�r��v��K.��.������#|/��n�+.��K.���.>�n����ܧ�]���K.��.��뎞==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO�<z~�+���K.��.���Go���=z{������?����+���K.��.����/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/�����g_��nq�[��+���K.��K.�����?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?�;�?��������t���6���mns�[�����+���K.��.����{{7��w�.����6���mnq�[��W\q�W\r�%�\r�\|�w/��������܃���6���-nq�[��+���K.��K.������~���}7~���>]��=��mns�������-���+���K.��.������y7���O�{pns����6���-nq�+���+.��K.���.�����?��7����>\��mns�������-nq�W\q�%�\r�%\p����������_�����6���mnq�[��W\q�W\r�%�\r�\p�=��?￼���>\��mns�������-nq�W\q�%�\r�%\p��s��w�?����r.��6���mns�[�����+���K.��.�����dx��}�܃���6���-nq�[��+���K.��K.���.>���{�t�[n������-���+���K.��.��<������n��w�[�����+���K.��.����|/�Ow���	nq�[��+���K.��K.������|�Ow���[�����+���K.��.����|/���ݮ}�_����~y�/o��m��헷��_����~y�/o��m��헷��_����~y�/m����G�m����G�m����G�m����G�m����G�m����G�m?����C�m?����C�m?����C�m?����C�m?����C�m?����C��������������������������������������������������߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭���������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������_�����      D      x������ � �      A   ,   x�3�LL��̃�%\F��%�EP��˘�-�(B�q��qqq ��&     