CREATE SEQUENCE public.tt_user_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.tt_user_sequence
  OWNER TO postgres;

CREATE SEQUENCE public.tt_profile_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.tt_profile_sequence
  OWNER TO postgres;

CREATE SEQUENCE public.tt_order_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.tt_order_sequence
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.generic_stamp()
  RETURNS trigger AS
$BODY$
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.generic_stamp()
  OWNER TO postgres;

CREATE TABLE public.wx_user
(
  id_ bigint NOT NULL,
  create_date_ timestamp with time zone,
  modify_date_ timestamp with time zone,
  phone_ text,
  sms_code_ text,
  password_ text,
  role_ text,
  confirmed_ boolean DEFAULT false,
  enabled_ boolean DEFAULT false,
  CONSTRAINT wx_user_pkey PRIMARY KEY (id_)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.wx_user
  OWNER TO postgres;

CREATE TRIGGER wx_user_timestamp
  BEFORE INSERT OR UPDATE
  ON public.wx_user
  FOR EACH ROW
  EXECUTE PROCEDURE public.generic_stamp();

CREATE TABLE public.tt_order
(
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
  sms_ boolean default false,
  CONSTRAINT tt_order_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_tt_order_creator FOREIGN KEY (creator_)
      REFERENCES public.wx_user (id_) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tt_order
  OWNER TO postgres;

CREATE TRIGGER tt_order_timestamp
  BEFORE INSERT OR UPDATE
  ON public.tt_order
  FOR EACH ROW
  EXECUTE PROCEDURE public.generic_stamp();

CREATE TABLE public.wx_profile
(
  id_ bigint NOT NULL,
  create_date_ timestamp with time zone,
  modify_date_ timestamp with time zone,
  user_ bigint,
  wallet_ bigint,
  template_ text,
  CONSTRAINT wx_profile_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_wx_profile_user FOREIGN KEY (user_)
      REFERENCES public.wx_user (id_) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.wx_profile
  OWNER TO postgres;

CREATE TRIGGER wx_profile_timestamp
  BEFORE INSERT OR UPDATE
  ON public.wx_profile
  FOR EACH ROW
  EXECUTE PROCEDURE public.generic_stamp();