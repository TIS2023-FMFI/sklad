PGDMP  "    ;                 |            storage    16.0    16.0 G    L           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            M           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            N           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            O           1262    16486    storage    DATABASE     |   CREATE DATABASE storage WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
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
       public         heap    postgres    false            P           0    0    COLUMN history.truck_income    COMMENT     k   COMMENT ON COLUMN public.history.truck_income IS 'poznamka, ci kamion prisiel s paletami alebo odišiel
';
          public          postgres    false    224            Q           0    0    COLUMN history.truck_number    COMMENT     C   COMMENT ON COLUMN public.history.truck_number IS 'číslo točky';
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
            public          postgres    false    216            A          0    16558    customer 
   TABLE DATA           ^   COPY public.customer (id, name, address, city, postal_code, ico_value, dic_value) FROM stdin;
    public          postgres    false    222   �T       B          0    16567    customer_reservation 
   TABLE DATA           k   COPY public.customer_reservation (id, id_customer, reserved_from, reserved_until, id_position) FROM stdin;
    public          postgres    false    223   uU       C          0    16588    history 
   TABLE DATA           o   COPY public.history (id, id_customer, "time", date, truck_income, number_of_pallets, truck_number) FROM stdin;
    public          postgres    false    224   %v       @          0    16527    material 
   TABLE DATA           ,   COPY public.material (id, name) FROM stdin;
    public          postgres    false    221   Bv       =          0    16510    pallet 
   TABLE DATA           p   COPY public.pallet (pnr, date_income, is_damaged, id_user, type, note, weight, number_of_positions) FROM stdin;
    public          postgres    false    218   �v       G          0    16629    pallet_on_position 
   TABLE DATA           H   COPY public.pallet_on_position (id, id_pallet, id_position) FROM stdin;
    public          postgres    false    228   �v       <          0    16496    position 
   TABLE DATA           3   COPY public."position" (name, is_tall) FROM stdin;
    public          postgres    false    217   �v       >          0    16520    stored_on_pallet 
   TABLE DATA           I   COPY public.stored_on_pallet (id, pnr, id_product, quantity) FROM stdin;
    public          postgres    false    219   *�       ;          0    16488    users 
   TABLE DATA           :   COPY public.users (id, name, password, admin) FROM stdin;
    public          postgres    false    216   G�       R           0    0    customer_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.customer_id_seq', 37, true);
          public          postgres    false    225            S           0    0    customer_reservation_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.customer_reservation_id_seq', 5208, true);
          public          postgres    false    226            T           0    0    history_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.history_id_seq', 1, false);
          public          postgres    false    230            U           0    0    material_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.material_id_seq', 7, true);
          public          postgres    false    220            V           0    0    pallet_on_position_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.pallet_on_position_id_seq', 7, true);
          public          postgres    false    229            W           0    0    stored_on_position_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.stored_on_position_id_seq', 3, true);
          public          postgres    false    227            X           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 3, true);
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
       public            postgres    false    216            �           1259    16647    fki_fk_pallet    INDEX     Q   CREATE INDEX fki_fk_pallet ON public.pallet_on_position USING btree (id_pallet);
 !   DROP INDEX public.fki_fk_pallet;
       public            postgres    false    228            �           1259    16641    fki_fk_position    INDEX     U   CREATE INDEX fki_fk_position ON public.pallet_on_position USING btree (id_position);
 #   DROP INDEX public.fki_fk_position;
       public            postgres    false    228            �           1259    24838    fki_foreign_key_position    INDEX     `   CREATE INDEX fki_foreign_key_position ON public.customer_reservation USING btree (id_position);
 ,   DROP INDEX public.fki_foreign_key_position;
       public            postgres    false    223            �           1259    16583    foreign_key_customer    INDEX     \   CREATE INDEX foreign_key_customer ON public.customer_reservation USING btree (id_customer);
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
       public          postgres    false    222    4755    223            �           2606    16605 :   customer_reservation customer_reservation_id_position_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT customer_reservation_id_position_fkey FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 d   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT customer_reservation_id_position_fkey;
       public          postgres    false    223    4736    217            �           2606    16642    pallet_on_position fk_pallet    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_pallet FOREIGN KEY (id_pallet) REFERENCES public.pallet(pnr) NOT VALID;
 F   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT fk_pallet;
       public          postgres    false    218    4739    228            �           2606    16636    pallet_on_position fk_position    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet_on_position
    ADD CONSTRAINT fk_position FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 H   ALTER TABLE ONLY public.pallet_on_position DROP CONSTRAINT fk_position;
       public          postgres    false    4736    228    217            �           2606    24833 )   customer_reservation foreign_key_position    FK CONSTRAINT     �   ALTER TABLE ONLY public.customer_reservation
    ADD CONSTRAINT foreign_key_position FOREIGN KEY (id_position) REFERENCES public."position"(name) NOT VALID;
 S   ALTER TABLE ONLY public.customer_reservation DROP CONSTRAINT foreign_key_position;
       public          postgres    false    223    4736    217            �           2606    16594     history history_id_customer_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES public.customer(id) NOT VALID;
 J   ALTER TABLE ONLY public.history DROP CONSTRAINT history_id_customer_fkey;
       public          postgres    false    4755    222    224            �           2606    16534    pallet pallete_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pallet
    ADD CONSTRAINT pallete_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id) NOT VALID;
 E   ALTER TABLE ONLY public.pallet DROP CONSTRAINT pallete_id_user_fkey;
       public          postgres    false    218    216    4734            �           2606    16552 3   stored_on_pallet stored_on_position_id_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;
 ]   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_id_product_fkey;
       public          postgres    false    221    219    4749            �           2606    16546 ,   stored_on_pallet stored_on_position_pnr_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_fkey FOREIGN KEY (pnr) REFERENCES public.pallet(pnr) NOT VALID;
 V   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pnr_fkey;
       public          postgres    false    4739    219    218            A   �   x�m��
�0�瓧�	�i�6Y;J�����\Z�b�p[}{��� �;���4�+31�8,����YT��d�P	�MS�����62A�4�����(mM,J�U��_�?-m3�ں���:�Tɠ�Rxp���۰�r�(cp����djt;J������E̯w�
!^��=�      B      x���K���E�ysI`�,��F���#��
��"P��z�-Q:Iѥ������G���������8��F"�����Md�S�0��R��L|Jo&�P���6���~�X��ǟ6���������Ro�����+p����+Pm�:�@���
����8����
���+0l�W�6܁+P�?��{�^��i���e���cڤk��v�/�`�>i��QLu'k���d���:Y���r�v}������e�.����c�S���Oa�����[�)��ǴIO��`�v�'o"l�w�vK3�L�n��&{�����>�D��:޻��e�v��h%�ن�x�9\�w5֮/�b���Z�ݵX|��~Ylܐ-�l��2�l֮[��{��h'��Mz�e6�ͧ�;�����[},���ca�`nP=�a�k���^�f"���/�knE=����v��Sx�.k���X�>��ڵ6޻fTk�k���ja�>�d���X�Ӵ[x�>i���I�e0ϺV���5�6�X�Qe�6��XS]M���M,����[�E�v]u��izi�]sNjc����щ8��Lf ���2��3����1�k2�c��d6���d��Ƴ��xAF�֘ڦ맡���L��\}-��9�g�2�A=;��������z>��zv43Գ��ɰ�O�O��<}���|���|f2��C�A=W�Eg=�>��zvF3ֳ�k�����j<�g��z�3�y�~ۍ��$�Y�a���s�~���������l7��ŉzv`3�EcF=;���i��~�xX�[�I��:���O��d=�L쳏y�~�͜��c<�g��d=ˎ��>�ZL>�&�ٺ�z�>��z^���z�[���^+9}����/>�K,�sјy?W���\|<��|�������>�'ټ����z�z�g�$;��4�ϲ?����t���������Z�ٱ�dx?��]�g߫�`��{c��CcN����z6�3�ϧ��z^>��v���U��8���n��Kb7|^��|��Jb7\�%ѳƜ����\��DϮ��z����zv�fUֳ����R;$�z�Z���A=��������`�Z06�i�`��al�?/�����<X��̃ui<�g��WZ����ۡy����H�y��F1V���`�Y�<X�ƃznM�a=�_��kӘ=�]|�?�����bl�k-����b��/��Z�ֳւy�zۍ�y��e[��ւy�j-������>�b��%���H�y�ʗ`l�/��*�y�ʷal[sg=�߻��|���~�b,�?̃Msg���	ʎ1֘�Y6�y��26���*��y�i�16퍄�[f�Z/��*��y�io0V�l̃U�)�`�x�b���|-��*��y���el���1���16�H̃M��`u�e3�C2�g_��<X}M7�`u^�̃��k3֦w%�Y��spk��o,������y�:;l���~�f�~vo����.�Uc�s�cV�y�v�+��s�?�z1��7�`���f���̃m�]|��������<؇ޕ���vc�]��5Mx����<��fn���q��<��[flM�B=w��`�3w3�x��w�<�e3�/U��B=w����>�f�C�A=w���>�.ֳ�*�`��g�C�a=k�3v�7�`מg�ι�y���1v�v�{јY������x����)�x�	��ă����ăW��6�����v�j<��'r��x����&|~��x���.��s�$ă���GwN�ă�ѥg��/r%�x��\�&�#氉��7m�Aӏ�<��1�M<�D�j�^��^?;O<�F�a�^�y>��h���u'|"׶�}��]��7b �x���d����!��*��u �x��:�M<x�n����Văw0�&�#?����1�ă�����y�~^�A�A��g���~�|���~��7�=��)���U=��$�Y�A={>�d@ϯ�&z��K���<���~��ep?{l�d���w%���E<x�>�dp?{�dX�1/��6��=����K>�ɀ�ŖM���י��O1��ߐ��$��9ƌzvf4����aNY�{'��4����&��y���oq�ɰ�14f�����ࣚ
�I��y��h�Qă���-ă��	&���k3L�����d�>��s'�T�i2�g��0��g�����/�L&��>��������ă�oo>�[6�}I&��\?ă��&�~]��#|T�l2�o��C���}�ă�j�M�s�]%������ă�o-��ƌ��������x���;��:a�I���"|~~��ܟ��~?�y0|���m�_��L���a�N�"����O�Z���ơ=F<x��g��������a�Nv�x�R��ɠ��x����ă���&|~��૜�ɰ}��E<輬w��?�����]�a�!�c���p|��a!4���`a�3������L�l���>�����K��K�^����uq&����SL��:���yp8/�A�5���^̃�j<��y�d�>kM�}��]h��N�ɠ����d���cNxpH��К^�2�Ϟ�0��ǈ}��܉/�=1��л��U�AC�D!�T�g2xz��ɠ�����K��>#*ă[���kzIx�cV�x�R�z/ăw���yP��<8ܯ+ă��r�B<����E<x)?��������ҘAϟj!z!�T����:�^�ź{!�T��ࣸz/ă��{�B<xG��^���x�Q���G��^�}o�9��]g�ॺ�^��lBƃ>w��!�J<��^[/ă����B<�����A�E�dT|�>���x����+�A��ă�j{!�T�K:+�/՝�B<x)'����$�zV��p~p��0��̃�����hK�=���׼�B<��NM/ăOğ��1��`�xXϞ(ă�j�z!�T���r7�y�w=��A�^��K5o��s2�ă�5��<�R�}6�����J<��^�W��Ow�{%�t�W��[wFz%�����$?X%���Ę�a������+��;#�^�}�x��P%|tw�W��K�\ze��^�/����U��<8��\�o���x�Vߌ^�^����ă���J<��WL�ă���J<x�^[�̃E��x�Qp�ă��.�z]\�ă�j�M&��z��8SW��K5�&�p���<X$�<8�.��95/�g�J<����C<x��\�?����ڍ3����x�E����;rJ��8?��{'|#�Q�͟�'�T�l2��O��x�ҽc����xK%�~{�yP�J<��W�ɠ�!��ăwpw%��a+��:4�aC6�y��<My��<M��):O�?՝�ڍ3���n�$�?�x��ǈ_��1�A�;ăo��*�ϘW�ݚڍC:$����dP�q���g�ăO��*׋zo��Ւ�r%����$?�� �~� ����%�^���E<x�8�y0|6����x��`%|T�n2	wK������,��x��1���7��;��x��<Q#�u/�d0^��Nc�Ո�`�F<����Lb�5��.='�7l6�A�u>f�A�-�ăO�[S�J��y��1'���;��Z�x�>�.&����jE��q�F<�E\�q~�ﭛs�ۖ��A��׋�n4��K=�L���n3�j�����1�3����<x:�6��.�0��7��7ΔF<�{U����:���A��5�A�i�j��r
�yP����A�Ce2h7�l��y��<Xe���!��^T6�x�R_,�A�N��x��\d��`�=�zQ��7�6�^����i\/z���3�`��`T�tKxP����<(�l̃��F<x�����9��^�ak2���Gb��½q~P��F<�Fn�q���q��E�?mI~�c��Uv��E�j�,z��#ޕ�g�O��4��;rٍ�=���g�$��ׂx��Z�F<x���$���C��Kw�M&�nO    �?ăoԼ5��'�z�x�������V�U��&;���果�z���c�y�tn�I���0t�A���̃��ԉ���������yh�|�8d8�qh<l74f��L�̃:S:�s�ă��՚��������N<��~�ɠ��3��qt��G��{'�"�Г���ϝx�k��.����̃��v�*n�9?�ugT��s����x���`'��;�d�ߐm���b��yP�j;��ϝx���r���U�e�����bԝx��]�މ߈cw��7�����rgTܸޑs�I~P{�x��ڹ�<��N<xG޳�o�ה�zo:���Ɣ��a2��V'�"VЉ��9t�?Xb�	�]|6��������U�Љ_�Y2��^�dx?��g�5�d?k�\��� �⽝�E��I~Pgw��;��x���N<xG�~��ă_�r;������A��;�T��'�d�kq�h��!|�g�d�StV�Q�����rm���������%z��x�~�t�<�B�̃��̃�]t���;��)���A�7:�����A݅�\/�ZО���_�<��)�x���0�x��;#�x����������A�}��;bԃyP����������ƃ<�X�H�n{��1�������A��������Aű�<5���d���w�~>4w�+�x�߈���7��x������d�S|Pu˃x����HxP{��E��	z,e��A�׋�vnp~P5f�yPy���U7t~d<�y��s���������5����#�z<j$�A��A<xGN|��Trp~���>8?��� |#�<�z�!���(C+���2���&�*&<�~2K�AC1����G���,���Eu�spQ�FR/*;��A�����_T1����_Tqȑ�������w��Խ������?��������1����σ�Eu�e$�E�w�U�n��fJ��[g
�;����i08?����z��>?�A<x���/6�����M�������Qăo�=\/�������H������q6q�8���:���k�A�/̃��0�^T��x�
�9���|ѓx���+'���Óx���3'��Q��L��l�p?�!攮�p\�k^|O�}��x���~2����_Tq����(�rr~P���yP���yPwN��x�g�'�ăW�o�̃��pr?�SN�A�N�A߇z�ˍ��6���'�h'���%N�A��Τ��~;�T��L�E�d�N��j�d����~2�=9?��ғyP��'��iO����~2�%>�k:����3��Hg�_Tv�yP�
N��?���A��?��`��:�9�j���h��'��'��'��N�/ڷ����������Ü25�Ǩw1�.�I<�E<�$��S�A�?z�S�^���s~0֔���Z0�}N�E��>�^4��UM�����>�����������d��J<�E���zQկ�|Pw�O���{�^4��$?�,sr~P�p&<�� �ބ�n��8O��M�/��C��~2q�p���^T=N�?��g�O�cg�O�И�϶���ߛ�!�����~2bƓ�*5����H3��5���	�I��_T�Ù��x�$�"�=���~�brQ�<'��q��<x:/O�A՘M��/j�&�bD�y�t���_T�c�yP�����"�ބ���=�C�o�og>a&����3�/Z��o�.��zQ�O�\/*�e&��CsO�A�`�[u�7�\�L�~w`>��̃��\/�8�$|"n39?����<�~x������EU{0�g�\/�{��EuO|r~P��d���c���eù��z�O�/��ߓx����$|"F=�������ʱN�Aű'��O�'��L�:[M�'���L�7�A��Ǽ8����d<e�9?��d<u6%�����K���V΃�x���L�A�@&���X����	���~2]6��b��<�ڹ��U;7�^Tq���U�:9?x�xC�b�,��|o�c_�yPq��<xj�����i2��������Kc��������b�3�/�4���wq~P���|o�s�x��:�����3�̃�՟�T�&<蹛�<���L��/��4���������������[��^tI&���I�:��=M��X��A�b-������`�1'�����E�lq��zC-��ݮ��U϶8?����<�Z�E<x�]�E<��Y2���]�~�1s?�?]|P�aWR/Z��D�>��{7^I?����ɨ������鴒~2U�I�gO�_�����Aݽ]ăO��.�A� /��;�/�����.�A߫��j��ă_|jq?q��zQ��,��/��U������}������x�u1�n��_�O�����e��{����{'ỸC�E<�FLf1�/�A}Gl1�>L�Ƽ8����ugz�zq?�L.�A�^.��_�J���L��ȶ$�dt.s?��8?��+��y��u�p~pȏJ��8�,�A��h<�~����*f��G���[c���]��&�7��sỸʃ��������S���ă��������$�d<����Xf%ߛp�^|����k<��A��	���<8����[IQ�E�_T���A��o�'�|�&�w�H蘭��������S���J�>��_TL�����P�����p����7���=��<��&|�^�������������N��l͋뾚����\������6���������7�g�	n=��[��z$��m���yq~�i�h�����m�'���N�G�g�f�n3w�	��x���b����$������l���c�OF�y'�A�L�ս�M<xE/�M<xE_�M<��9���8s7�G<'�Ϛ{���Zp����m���ⴛx��\�N����N<�E^f~�����&dǲ���9���*���%v�OFgP�.v�=z�=�'���~2�_��^T�56����7c��������M���d�֝�G�z�M<�_S�'3�^ă_���~2�l��Cv5�A����K��U���7<N��~2�W�	��x���Z����l��7�ԛx����&�ߩ��/��]�9�:�z�s�x]�Z�Q�����G����֔x��z�M<�E=�N��ʎ%�E�Nx3�g�N���$ߛ0N���:F�aN1�k2�}n.�đ��+8$�}{��Ŝbv�d���ޕ�uz����d�{�M����C:L�E��ă���L�����J�7a��ɰ����u2]��s0�������]���Mx<�d�s��+�'�4���s����xX�[��ܭ�p�������ׂx�ѽ~�a�Nzf�s�d�^[�Ǒ�I~���9?�wfM&��z�#-�9�Ϯ��[1a������i�	����&�߇�#�7��3绷��q���+�y���:����Mg%��z%�dd��{U�w�?Xe������lo:�z��k����>��v�~2��'�'3\�ă��!M�9e�z��AϿ���i�WrH��|�������s~��]�<��zQ���^���I�E��/�9z�Ḩ�|�=��13w�]��A������Ev������7�L&��\�̃E�D�_T{�x�V��p?s�E��Y�|oB�7����$?(��7qȯ��`�m�~2U�������2�n?���SL�����d������uz�}�^��MT='��zǟ�������,��&|�
׋z��d�SOR/���$�E{u�/c�������9�'������sد����A�{7���%�'�>I����x��=4Χhop~��7��E=�a2���ޕ�g�?�Of��,����,ă�b�&�q~?�
����$u_���=�s��1w�_�����U���6�p��G5�&�u2~�z>�dNј�>�I�[�a���J�Csg��xBI��x̪$ߛp��0�����?ؤC�'�4/������7���\�A�A�LR���!|UW`2��i-�_��M��[���v͝��4�=�'�at^.̃���U��$�Aٍ$?(�I<�{�� �   �|o�cD���T�C�A����(����&ߣw�)I?g��ߛ�&����=���=���
���;�&�}�e{�^Ty��<�t~16�;ă��q�06�cI?�I?���_��Mx]ʨ����������,�1�����翜֒      C      x������ � �      @   =   x�3���/�/R0600�2�r�lc���̼D.ΐ����ĒԢ��.3T~)W� ߒ�      =      x������ � �      G      x������ � �      <   Q  x�5�A�[���q���d����������i��F�c�=�O��'���_���O쇻�����-nq�+���+.��K.���.�������rs{~�[��W\q�W\r�%�\r�\p����������.���-���+���K.��.����_����fy~�[��W\q�W\r�%�\r�\p������r3=��-nq��|��'�\r�\p����|�r3<��-nq��˟��������K.��.����<�w!|�/��n�+.��K.���.>�n���w�r��v��K.��.������2|w/��n�+.��K.���.>�n���w�r��v��K.��.������#|/��n�+.��K.���.>�n����ܧ�]���K.��.��뎞==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO��==zz�����ӣ�GO�<z~�+���K.��.���Go���=z{������?����+���K.��.����/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/����n��w�[�����+���K.��.����|/�����g_��nq�[��+���K.��K.�����?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?����ϵ��s�k�\���?�;�?��������t���6���mns�[�����+���K.��.����{{7��w�.����6���mnq�[��W\q�W\r�%�\r�\|�w/��������܃���6���-nq�[��+���K.��K.������~���}7~���>]��=��mns�������-���+���K.��.������y7���O�{pns����6���-nq�+���+.��K.���.�����?��7����>\��mns�������-nq�W\q�%�\r�%\p����������_�����6���mnq�[��W\q�W\r�%�\r�\p�=��?￼���>\��mns�������-nq�W\q�%�\r�%\p��s��w�?����r.��6���mns�[�����+���K.��.�����dx��}�܃���6���-nq�[��+���K.��K.���.>���{�t�[n������-���+���K.��.��<������n��w�[�����+���K.��.����|/�Ow���	nq�[��+���K.��K.������|�Ow���[�����+���K.��.����|/���ݮ}�_����~y�/o��m��헷��_����~y�/o��m��헷��_����~y�/m����G�m����G�m����G�m����G�m����G�m����G�m?����C�m?����C�m?����C�m?����C�m?����C�m?����C��������������������������������������������������߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭߭���������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������_�����      >      x������ � �      ;   ,   x�3�LL��̃�%\F��%�EP��˘�-�(B�q��qqq ��&     