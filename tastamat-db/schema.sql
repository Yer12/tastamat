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
-- TOC entry 204 (class 1255 OID 16923)
-- Name: generic_stamp(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.generic_stamp() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
      NEW.create_date_ = now();
      NEW.modify_date_ = now();
      RETURN NEW;
    ELSE
      NEW.modify_date_ = now();
      RETURN NEW;
    END IF;
END;
$$;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 200 (class 1259 OID 16935)
-- Name: tt_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tt_order (
    id_ bigint NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    creator_ bigint,
    identificator_ text,
    recipient_name_ text,
    recipient_phone_ text,
    locker_id_ bigint,
    locker_code_ text,
    box_size_ text,
    drop_code_ text,
    drop_date_ timestamp with time zone,
    pick_code_ text,
    pick_date_ timestamp with time zone,
    status_ text,
    sms_ boolean DEFAULT false
);


--
-- TOC entry 198 (class 1259 OID 16921)
-- Name: tt_order_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tt_order_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 203 (class 1259 OID 16993)
-- Name: tt_payment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tt_payment (
    id_ bigint NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    identificator_ text,
    user_ bigint,
    amount_ bigint,
    pid_ text,
    status_ text
);


--
-- TOC entry 202 (class 1259 OID 16977)
-- Name: tt_payment_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tt_payment_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 197 (class 1259 OID 16919)
-- Name: tt_profile_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tt_profile_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 196 (class 1259 OID 16917)
-- Name: tt_user_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tt_user_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 201 (class 1259 OID 16950)
-- Name: wx_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wx_profile (
    id_ bigint NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    user_ bigint,
    wallet_ bigint,
    template_ text
);


--
-- TOC entry 199 (class 1259 OID 16924)
-- Name: wx_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wx_user (
    id_ bigint NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    phone_ text,
    sms_code_ text,
    password_ text,
    role_ text,
    confirmed_ boolean DEFAULT false,
    enabled_ boolean DEFAULT false
);


--
-- TOC entry 3566 (class 2606 OID 16943)
-- Name: tt_order tt_order_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tt_order
    ADD CONSTRAINT tt_order_pkey PRIMARY KEY (id_);


--
-- TOC entry 3570 (class 2606 OID 17000)
-- Name: tt_payment tt_payment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tt_payment
    ADD CONSTRAINT tt_payment_pkey PRIMARY KEY (id_);


--
-- TOC entry 3568 (class 2606 OID 16957)
-- Name: wx_profile wx_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_profile
    ADD CONSTRAINT wx_profile_pkey PRIMARY KEY (id_);


--
-- TOC entry 3564 (class 2606 OID 16933)
-- Name: wx_user wx_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_user
    ADD CONSTRAINT wx_user_pkey PRIMARY KEY (id_);


--
-- TOC entry 3575 (class 2620 OID 16949)
-- Name: tt_order tt_order_timestamp; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER tt_order_timestamp BEFORE INSERT OR UPDATE ON public.tt_order FOR EACH ROW EXECUTE PROCEDURE public.generic_stamp();


--
-- TOC entry 3577 (class 2620 OID 17006)
-- Name: tt_payment tt_payment_timestamp; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER tt_payment_timestamp BEFORE INSERT OR UPDATE ON public.tt_payment FOR EACH ROW EXECUTE PROCEDURE public.generic_stamp();


--
-- TOC entry 3576 (class 2620 OID 16963)
-- Name: wx_profile wx_profile_timestamp; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER wx_profile_timestamp BEFORE INSERT OR UPDATE ON public.wx_profile FOR EACH ROW EXECUTE PROCEDURE public.generic_stamp();


--
-- TOC entry 3574 (class 2620 OID 16934)
-- Name: wx_user wx_user_timestamp; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER wx_user_timestamp BEFORE INSERT OR UPDATE ON public.wx_user FOR EACH ROW EXECUTE PROCEDURE public.generic_stamp();


--
-- TOC entry 3571 (class 2606 OID 16944)
-- Name: tt_order fk_tt_order_creator; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tt_order
    ADD CONSTRAINT fk_tt_order_creator FOREIGN KEY (creator_) REFERENCES public.wx_user(id_);


--
-- TOC entry 3573 (class 2606 OID 17001)
-- Name: tt_payment fk_tt_payment_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tt_payment
    ADD CONSTRAINT fk_tt_payment_user FOREIGN KEY (user_) REFERENCES public.wx_user(id_);


--
-- TOC entry 3572 (class 2606 OID 16958)
-- Name: wx_profile fk_wx_profile_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_profile
    ADD CONSTRAINT fk_wx_profile_user FOREIGN KEY (user_) REFERENCES public.wx_user(id_);
