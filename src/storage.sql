PGDMP      .                 |            storage    16.0    16.0     
           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16486    storage    DATABASE     |   CREATE DATABASE storage WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Slovak_Slovakia.1250';
    DROP DATABASE storage;
                postgres    false            �            1259    16520    stored_on_pallet    TABLE     �   CREATE TABLE public.stored_on_pallet (
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
            public          postgres    false    219                      0    16520    stored_on_pallet 
   TABLE DATA           I   COPY public.stored_on_pallet (id, pnr, id_product, quantity) FROM stdin;
    public          postgres    false    219   )                  0    0    stored_on_position_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.stored_on_position_id_seq', 26, true);
          public          postgres    false    227            r           2606    16587 (   stored_on_pallet stored_on_position_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pkey;
       public            postgres    false    219            t           2606    16585 6   stored_on_pallet stored_on_position_pnr_id_product_key 
   CONSTRAINT     |   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_pnr_id_product_key UNIQUE (pnr, id_product);
 `   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_pnr_id_product_key;
       public            postgres    false    219    219            n           1259    24897    fki_FK_idPallet    INDEX     M   CREATE INDEX "fki_FK_idPallet" ON public.stored_on_pallet USING btree (pnr);
 %   DROP INDEX public."fki_FK_idPallet";
       public            postgres    false    219            o           1259    16557    foreign_key_material    INDEX     W   CREATE INDEX foreign_key_material ON public.stored_on_pallet USING btree (id_product);
 (   DROP INDEX public.foreign_key_material;
       public            postgres    false    219            p           1259    16551    foreign_key_pnr    INDEX     K   CREATE INDEX foreign_key_pnr ON public.stored_on_pallet USING btree (pnr);
 #   DROP INDEX public.foreign_key_pnr;
       public            postgres    false    219            u           2606    24892    stored_on_pallet FK_idPallet    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT "FK_idPallet" FOREIGN KEY (pnr) REFERENCES public.pallet(pnr) ON DELETE CASCADE NOT VALID;
 H   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT "FK_idPallet";
       public          postgres    false    219            v           2606    16552 3   stored_on_pallet stored_on_position_id_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stored_on_pallet
    ADD CONSTRAINT stored_on_position_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.material(id) NOT VALID;
 ]   ALTER TABLE ONLY public.stored_on_pallet DROP CONSTRAINT stored_on_position_id_product_fkey;
       public          postgres    false    219                  x�32�4201�44�4�����  �     