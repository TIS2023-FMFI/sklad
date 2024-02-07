PGDMP                      |            storage    16.0    16.0 G    M           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            N           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            O           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            P           1262    24593    storage    DATABASE     |   CREATE DATABASE storage WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
    DROP DATABASE storage;
                postgres    false            �            1259    25963    customer    TABLE     �   CREATE TABLE public.customer (
    id integer NOT NULL,
    name text NOT NULL,
    address text NOT NULL,
    city text NOT NULL,
    postal_code text NOT NULL,
    ico_value text,
    dic_value text,
    is_root boolean DEFAULT false
);
    DROP TABLE public.customer;
       public         heap    postgres    false            �            1259    25968    customer_id_seq    SEQUENCE     �   ALTER TABLE public.customer ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    215            �            1259    25969    customer_reservation    TABLE     �   CREATE TABLE public.customer_reservation (
    id integer NOT NULL,
    id_customer integer NOT NULL,
    reserved_from date NOT NULL,
    reserved_until date NOT NULL,
    id_position text NOT NULL
);
 (   DROP TABLE public.customer_reservation;
       public         heap    postgres    false            �            1259    25974    customer_reservation_id_seq    SEQUENCE     �   ALTER TABLE public.customer_reservation ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customer_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    217            �            1259    25975    history    TABLE       CREATE TABLE public.history (
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
          public          postgres    false    219            R           0    0    COLUMN history.truck_number    COMMENT     C   COMMENT ON COLUMN public.history.truck_number IS 'číslo točky';
          public          postgres    false    219            �            1259    25979    history_id_seq    SEQUENCE     �   ALTER TABLE public.history ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    219            �            1259    25980    material    TABLE     R   CREATE TABLE public.material (
    id integer NOT NULL,
    name text NOT NULL
);
    DROP TABLE public.material;
       public         heap    postgres    false            �            1259    25985    material_id_seq    SEQUENCE     �   ALTER TABLE public.material ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.material_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    221            �            1259    25986    pallet    TABLE     +  CREATE TABLE public.pallet (
    pnr text NOT NULL,
    date_income date NOT NULL,
    is_damaged boolean DEFAULT false NOT NULL,
    id_user integer,
    type text DEFAULT 'europaleta'::text NOT NULL,
    note text,
    weight double precision NOT NULL,
    number_of_positions integer NOT NULL
);
    DROP TABLE public.pallet;
       public         heap    postgres    false            �            1259    25993    pallet_on_position    TABLE     �   CREATE TABLE public.pallet_on_position (
    id integer NOT NULL,
    id_pallet text NOT NULL,
    id_position text NOT NULL
);
 &   DROP TABLE public.pallet_on_position;
       public         heap    postgres    false            �            1259    25998    pallet_on_position_id_seq    SEQUENCE     �   ALTER TABLE public.pallet_on_position ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pallet_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    224            �            1259    25999    position    TABLE     g   CREATE TABLE public."position" (
    name text NOT NULL,
    is_tall boolean DEFAULT false NOT NULL
);
    DROP TABLE public."position";
       public         heap    postgres    false            �            1259    26005    stored_on_pallet    TABLE     �   CREATE TABLE public.stored_on_pallet (
    id integer NOT NULL,
    pnr text NOT NULL,
    id_product integer NOT NULL,
    quantity integer DEFAULT 1 NOT NULL
);
 $   DROP TABLE public.stored_on_pallet;
       public         heap    postgres    false            �            1259    26011    stored_on_position_id_seq    SEQUENCE     �   ALTER TABLE public.stored_on_pallet ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.stored_on_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    227            �            1259    26012    users    TABLE     u   CREATE TABLE public.users (
    id integer NOT NULL,
    name text NOT NULL,
    password text,
    admin boolean
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    26017    user_id_seq    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    229            ;          0    25963    customer 
   TABLE DATA           g   COPY public.customer (id, name, address, city, postal_code, ico_value, dic_value, is_root) FROM stdin;
    public          postgres    false    215   T       =          0    25969    customer_reservation 
   TABLE DATA           k   COPY public.customer_reservation (id, id_customer, reserved_from, reserved_until, id_position) FROM stdin;
    public          postgres    false    217   T       ?          0    25975    history 
   TABLE DATA           o   COPY public.history (id, id_customer, "time", date, truck_income, number_of_pallets, truck_number) FROM stdin;
    public          postgres    false    219   �V       A          0    25980    material 
   TABLE DATA           ,   COPY public.material (id, name) FROM stdin;
    public          postgres    false    221   UW       C          0    25986    pallet 
   TABLE DATA           p   COPY public.pallet (pnr, date_income, is_damaged, id_user, type, note, weight, number_of_positions) FROM stdin;
    public          postgres    false    223   �W       D          0    25993    pallet_on_position 
   TABLE DATA           H   COPY public.pallet_on_position (id, id_pallet, id_position) FROM stdin;
    public          postgres    false    224   @X       F          0    25999    position 
   TABLE DATA           3   COPY public."position" (name, is_tall) FROM stdin;
    public          postgres    false    226   �X       G          0    26005    stored_on_pallet 
   TABLE DATA           I   COPY public.stored_on_pallet (id, pnr, id_product, quantity) FROM stdin;
    public          postgres    false    227   �c       I          0    26012    users 
   TABLE DATA           :   COPY public.users (id, name, password, admin) FROM stdin;
    public          postgres    false    229   Dd       S           0    0    customer_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.customer_id_seq', 43, true);
          public          postgres    false    216            T           0    0    customer_reservation_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.customer_reservation_id_seq', 7523, true);
          public          postgres    false    218            U           0    0    history_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.history_id_seq', 15, true);
          public          postgres    false    220            V           0    0    material_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.material_id_seq', 15, true);
          public          postgres    false    222            W           0    0    pallet_on_position_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.pallet_on_position_id_seq', 19, true);
          public          postgres    false    225            X           0    0    stored_on_position_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.stored_on_position_id_seq', 13, true);
          public          postgres    false    228            Y           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 6, true);
          public          postgres    false    230            }           2606    26019    customer customer_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_name_key;
       public            postgres    false    215                       2606    26021    customer customer_name_key1 
   CONSTRAINT     V   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_name_key1 UNIQUE (name);
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_name_key1;
       public            postgres    false    215            �           2606    26023    customer customer_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public            postgres    false    215            �           2606    26025 .   customer_reservation customer_reservation_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_pkey;
       public            postgres    false    217            �           2606    26027    history history_pk 
   CONSTRAINT     P   ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_pk PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.history DROP CONSTRAINT history_pk;
       public            postgres    false    219            �           2606    26029    material material_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.material DROP CONSTRAINT material_name_key;
       public            postgres    false    221            �           2606    26031    material material_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.material DROP CONSTRAINT material_pkey;
       public            postgres    false    221            �           2606    26033 ?   pallet_on_position pallet_on_position_id_pallet_id_position_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT pallet_on_position_id_pallet_id_position_key UNIQUE (id_pallet, id_position);
 i   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT pallet_on_position_id_pallet_id_position_key;
       public            postgres    false    224    224            �           2606    26035    pallet pallete_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_pkey PRIMARY KEY (pnr);
 =   ALTER TABLE ONLY public.pallet DROP CONSTRAINT pallete_pkey;
       public            postgres    false    223            �           2606    26037    pallet_on_position pk 
   CONSTRAINT     S   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT pk PRIMARY KEY (id);
 ?   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT pk;
       public            postgres    false    224            �           2606    26039    position position_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (name);
 B   ALTER TABLE ONLY public."position" DROP CONSTRAINT position_pkey;
       public            postgres    false    226            �           2606    26041 (   stored_on_pallet stored_on_position_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pkey;
       public            postgres    false    227            �           2606    26043 6   stored_on_pallet stored_on_position_pnr_id_product_key 
   CONSTRAINT     |   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_id_product_key UNIQUE (pnr, id_product);
 `   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pnr_id_product_key;
       public            postgres    false    227    227            �           2606    26045    users user_name_key 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_name_key UNIQUE (name);
 =   ALTER TABLE ONLY public.users DROP CONSTRAINT user_name_key;
       public            postgres    false    229            �           2606    26047    users user_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 9   ALTER TABLE ONLY public.users DROP CONSTRAINT user_pkey;
       public            postgres    false    229            �           1259    26048    fki_fk_pallet    INDEX     Q   CREATE INDEX fki_fk_pallet ON public.pallet_on_position USING btree (id_pallet);
 !   DROP INDEX public.fki_fk_pallet;
       public            postgres    false    224            �           1259    26049    fki_fk_position    INDEX     U   CREATE INDEX fki_fk_position ON public.pallet_on_position USING btree (id_position);
 #   DROP INDEX public.fki_fk_position;
       public            postgres    false    224            �           1259    26050    fki_foreign_key_position    INDEX     `   CREATE INDEX fki_foreign_key_position ON public.customer_reservation USING btree (id_position);
 ,   DROP INDEX public.fki_foreign_key_position;
       public            postgres    false    217            �           1259    26051    fki_id_customer    INDEX     W   CREATE INDEX fki_id_customer ON public.customer_reservation USING btree (id_customer);
 #   DROP INDEX public.fki_id_customer;
       public            postgres    false    217            �           1259    26052    foreign_key_customer    INDEX     \   CREATE INDEX foreign_key_customer ON public.customer_reservation USING btree (id_customer);
 (   DROP INDEX public.foreign_key_customer;
       public            postgres    false    217            �           1259    26053    foreign_key_history    INDEX     N   CREATE INDEX foreign_key_history ON public.history USING btree (id_customer);
 '   DROP INDEX public.foreign_key_history;
       public            postgres    false    219            �           1259    26054    foreign_key_material    INDEX     W   CREATE INDEX foreign_key_material ON public.stored_on_pallet USING btree (id_product);
 (   DROP INDEX public.foreign_key_material;
       public            postgres    false    227            �           1259    26055    foreign_key_pnr    INDEX     K   CREATE INDEX foreign_key_pnr ON public.stored_on_pallet USING btree (pnr);
 #   DROP INDEX public.foreign_key_pnr;
       public            postgres    false    227            �           1259    26056    foreign_key_position    INDEX     \   CREATE INDEX foreign_key_position ON public.customer_reservation USING btree (id_position);
 (   DROP INDEX public.foreign_key_position;
       public            postgres    false    217            �           1259    26057    foreign_key_user    INDEX     F   CREATE INDEX foreign_key_user ON public.pallet USING btree (id_user);
 $   DROP INDEX public.foreign_key_user;
       public            postgres    false    223            �           2606    26115 :   customer_reservation customer_reservation_id_position_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) ON DELETE CASCADE;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_position_fkey;
       public          postgres    false    226    217    4761            �           2606    26063    pallet_on_position fk_pallet    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_pallet FOREIGN KEY (id_pallet) REFERENCES public.pallet(pnr) NOT VALID;
 F   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT fk_pallet;
       public          postgres    false    4753    223    224            �           2606    26120    pallet_on_position fk_position    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_position FOREIGN KEY (id_position) REFERENCES public."position"(name) ON DELETE CASCADE;
 H   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT fk_position;
       public          postgres    false    224    4761    226            �           2606    26078     customer_reservation id_customer    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT id_customer FOREIGN KEY (id_customer) REFERENCES public.customer(id) ON DELETE CASCADE NOT VALID;
 J   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT id_customer;
       public          postgres    false    4737    215    217            �           2606    26083    history id_customer    FK CONSTRAINT     �   ALTER TABLE ONLY public.history
    ADD CONSTRAINT id_customer FOREIGN KEY (id_customer) REFERENCES public.customer(id) ON DELETE CASCADE NOT VALID;
 =   ALTER TABLE ONLY public.history DROP CONSTRAINT id_customer;
       public          postgres    false    4737    219    215            �           2606    26109    pallet pallete_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE SET NULL;
 E   ALTER TABLE ONLY public.pallet DROP CONSTRAINT pallete_id_user_fkey;
       public          postgres    false    223    4771    229            �           2606    26093 3   stored_on_pallet stored_on_position_id_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;
 ]   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_id_product_fkey;
       public          postgres    false    4750    227    221            �           2606    26098 ,   stored_on_pallet stored_on_position_pnr_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_fkey FOREIGN KEY (pnr) REFERENCES public.pallet(pnr) NOT VALID;
 V   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pnr_fkey;
       public          postgres    false    223    227    4753            ;   m   x�36�t�/J���K,*�N�t*J,�,�I,K�44251���L�21�tOMK�W��/K��LT(�+�����P�04�74��.)J=қ��i`h�`d��B%\1z\\\ <��      =     x���;n1Dk�]P��O���9��5��s1�P���<��W����/�/�?}��V�]�y��K����"�@M��L�4O�BM�:�P�Y��Yu5#�U+�K�|k���y.�a�C=3�./����ȋ9{�5�9GMs��0�"s���l3�՘sW�|�Q�sn�Ü�}5�<���ߟYās�=���̹��!Ϫ�{�W�,�C��Ŝ5�`Α^��yu���̽3�3�̹�s�γ�밟�u�����\�a�%5�9��yp�G�<�sW��~V���oM�,Ǝ�y�v��8��G�l��G����Wr�;����<��&��x���q�5�N�9[W��Yo�ɜuWN�lM^��:pN�Ŝ=�r.�^�9�M��s�+����=�9�T��Yo��y��g�|g��o�0��[j�sz�1�*/����{���y.�B����0g��ٖ�9�L����-GAξT9�"r.�&���Mu{Cu�s��{��r��?%�����s����yn��F\���u��t�      ?   �   x�e�K� �5ܥ��r�Y{��MM&�����L�%����F����d�p[Y'�0Z�s��+�`	��7�w,�5x��'3�)�9�/���c�o��-�Ź32M���-�d:�tɺ{�>G��n�}���y���r��l�T��ا�����A�?U�Pf      A   u   x�U�1�0F���)z7q*!�c��$(u�^��{*�����+���9�?�=��,�h�5w�P�(�^p��$��C��M����XZ��N�����Ƌ��t�&͘v �=d&�      C   V   x�}�1� ���.�RO�-\t�Dc������_�Vd����"��>�����D4��T"4P�P�K�C�@��;�zC��&3� w�+o      D   C   x�34�4200�t4006�24�@<3 �γ ��@<Ng��9�g�Yp���M���L�b���� w�      F   Q  x�5�A�[���q���d����������i��F�c�=�O��'���_���O쇻�����-nq�+���+.��K.���.�������rs{~�[��W\q�W\r�%�\r�\p����������.���-���+���K.��.����_����fy~�[��W\q�W\r�%�\r�\p������r3=��-nq��|��'�\r�\p����|�r3<��-nq��˟��������K.��.����<�w!|�/��n�+.��K.���.>�n���w�r��v��K.��.������2|w/��n�+.��K.���.>�n���w�r��v��K.��.������#|/��n�+.��K.���.>�n����ܧ�]���K.��.��뎞==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO�<z~�+���K.��.���Go���=z{������?����+���K.��.����/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/�����g_��nq�[��+���K.��K.�����?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?�;�?��������t���6���mns�[�����+���K.��.����{{7��w�.����6���mnq�[��W\q�W\r�%�\r�\|�w/��������܃���6���-nq�[��+���K.��K.������~���}7~���>]��=��mns�������-���+���K.��.������y7���O�{pns����6���-nq�+���+.��K.���.�����?��7����>\��mns�������-nq�W\q�%�\r�%\p����������_�����6���mnq�[��W\q�W\r�%�\r�\p�=��?￼���>\��mns�������-nq�W\q�%�\r�%\p��s��w�?����r.��6���mns�[�����+���K.��.�����dx��}�܃���6���-nq�[��+���K.��K.���.>���{�t�[n������-���+���K.��.��<������n��w�[�����+���K.��.����|/�Ow���	nq�[��+���K.��K.������|�Ow���[�����+���K.��.����|/���ݮ}�_����~y�/o��m��헷��_����~y�/o��m��헷��_����~y�/m����G�m����G�m����G�m����G�m����G�m����G�m?����C�m?����C�m?����C�m?����C�m?����C�m?����C��������������������������������������������������߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭���������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������_�����      G   @   x�-��	�0��0�rl����=�8���
*��ҕ�l�Pt�I���{<����      I   ,   x�3�LL��̃�%\F��%�EP��˘�-�(B�q��qqq ��&     