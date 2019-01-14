-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS public.kpm_event CASCADE;

CREATE TABLE public.kpm_event
(
  id_              BIGINT,
  create_date_     TIMESTAMP WITH TIME ZONE,
  user_id_         BIGINT,
  event_type_      TEXT,
  event_info_      TEXT,
  fullname_        TEXT,
  email_           TEXT,
  ip_address_      TEXT,
  user_agent_      TEXT,
  container_id_    BIGINT,
  container_class_ TEXT,
  event_data_      TEXT,
  event_group_     TEXT,
  CONSTRAINT pk_kpm_event PRIMARY KEY (id_)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.kpm_event
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_group CASCADE;

CREATE TABLE public.wx_group
(
  create_date_ TIMESTAMP WITH TIME ZONE,
  modify_date_ TIMESTAMP WITH TIME ZONE,
  enabled_     BOOLEAN,
  removed_     BOOLEAN,
  name_kk_     TEXT,
  name_ru_     TEXT,
  name_en_     TEXT,
  id_          BIGINT,
  CONSTRAINT wx_group_pkey PRIMARY KEY (id_)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_group
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_user CASCADE;

CREATE TABLE public.wx_user
(
  id_               BIGINT,
  email_            TEXT,
  create_date_      TIMESTAMP WITH TIME ZONE,
  modify_date_      TIMESTAMP WITH TIME ZONE,
  enabled_          BOOLEAN,
  removed_          BOOLEAN,
  lastname_         TEXT,
  firstname_        TEXT,
  patronymic_       TEXT,
  status_           TEXT,
  creator_          BIGINT,
  role_             TEXT,
  branch_           BIGINT,
  mobile_phone_     TEXT,
  phone_            TEXT,
  additional_       TEXT,
  default_language_ TEXT,
  photo_            BIGINT,
  CONSTRAINT wx_user_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_wx_user_wx_group_branch FOREIGN KEY (branch_)
  REFERENCES public.wx_group (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_wx_user_wx_user_creator FOREIGN KEY (creator_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_user
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_membership CASCADE;

CREATE TABLE public.wx_membership
(
  enabled_    BOOLEAN,
  group_name_ CHARACTER VARYING(100) NOT NULL,
  user_id_    BIGINT                 NOT NULL,
  role_name_  CHARACTER VARYING(100) NOT NULL,
  CONSTRAINT wx_membership_pkey PRIMARY KEY (group_name_, user_id_),
  CONSTRAINT fkc17dd7f4d28b756e FOREIGN KEY (user_id_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_membership
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_file CASCADE;

CREATE TABLE public.wx_file
(
  id_              BIGINT  NOT NULL,
  filename_        TEXT,
  folder_          TEXT,
  create_date_     TIMESTAMP WITH TIME ZONE,
  modify_date_     TIMESTAMP WITH TIME ZONE,
  creator_         BIGINT,
  size_            BIGINT,
  duration_        BIGINT,
  height_          DOUBLE PRECISION,
  width_           DOUBLE PRECISION,
  storage_         TEXT,
  path_            TEXT,
  external_url_    TEXT,
  description_     TEXT,
  md5_hash_        TEXT,
  content_type_    TEXT,
  removed_         BOOLEAN NOT NULL DEFAULT FALSE,
  container_id_    BIGINT,
  container_class_ TEXT,
  CONSTRAINT pk_wx_file PRIMARY KEY (id_),
  CONSTRAINT wx_file_user_fk FOREIGN KEY (creator_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_file
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_dictionary CASCADE;

CREATE TABLE public.wx_dictionary
(
  code_    CHARACTER VARYING(20) NOT NULL,
  name_ru_ CHARACTER VARYING(300),
  name_kk_ CHARACTER VARYING(300),
  name_en_ CHARACTER VARYING(300),
  CONSTRAINT wx_dictionary_pk PRIMARY KEY (code_)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_dictionary
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_field CASCADE;

CREATE TABLE public.wx_field
(
  dictionary_code_ CHARACTER VARYING      NOT NULL,
  key_             CHARACTER VARYING(100) NOT NULL,
  ru_              TEXT,
  kk_              TEXT,
  en_              TEXT,
  comment_         TEXT,
  enabled_         BOOLEAN                NOT NULL DEFAULT FALSE,
  removed_         BOOLEAN                NOT NULL DEFAULT FALSE,
  create_date_     TIMESTAMP WITH TIME ZONE,
  modify_date_     TIMESTAMP WITH TIME ZONE,
  CONSTRAINT wx_field_pk PRIMARY KEY (dictionary_code_, key_)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_field
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_mail_info CASCADE;

CREATE TABLE public.wx_mail_info
(
  id_          BIGINT,
  email_       TEXT,
  create_date_ TIMESTAMP WITH TIME ZONE,
  send_date_   TIMESTAMP WITH TIME ZONE,
  send_result_ TEXT,
  subject_     TEXT,
  message_     TEXT,
  succeeded_   BOOLEAN,
  CONSTRAINT pk_mail_info PRIMARY KEY (id_)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_mail_info
  OWNER TO postgres;

DROP TABLE IF EXISTS public.kpm_subscription CASCADE;

CREATE TABLE public.kpm_subscription
(
  id_          BIGINT,
  create_date_ TIMESTAMP WITH TIME ZONE,
  modify_date_ TIMESTAMP WITH TIME ZONE,
  type_        TEXT,
  send_type_   TEXT,
  status_      TEXT,
  user_        BIGINT,
  CONSTRAINT kpm_notification_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_kpm_notification_wx_user_user FOREIGN KEY (user_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.kpm_subscription
  OWNER TO postgres;

DROP TABLE IF EXISTS public.kpm_material CASCADE;

CREATE TABLE public.kpm_material
(
  id_              BIGINT,
  create_date_     TIMESTAMP WITH TIME ZONE,
  modify_date_     TIMESTAMP WITH TIME ZONE,
  name_            CHARACTER VARYING(100),
  direction_       CHARACTER VARYING(50),
  age_             CHARACTER VARYING(5),
  description_     CHARACTER VARYING(500),
  parent_          BIGINT,
  source_          CHARACTER VARYING(20),
  text_            TEXT,
  size_            BIGINT,
  creator_         BIGINT,
  removed_         BOOLEAN,
  marks_           CHARACTER VARYING [],
  languages_       CHARACTER VARYING [],
  status_          CHARACTER VARYING,
  url_             TEXT,
  gender_target_   CHARACTER VARYING(10),
  is_draft_        BOOLEAN DEFAULT FALSE,
  type_            TEXT,
  moderator_       BIGINT,
  file_id_         BIGINT,
  group_id_        BIGINT,
  target_ages_     CHARACTER VARYING [],
  involvements     CHARACTER VARYING [],
  approve_comment_ TEXT,
  fix_comment_     TEXT,
  forbid_comment_  TEXT,
  CONSTRAINT kpm_material_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_7o1vm9jl0x3ujjxta7nys3vel FOREIGN KEY (parent_)
  REFERENCES public.kpm_material (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_kpm_material_wx_user_moderator FOREIGN KEY (moderator_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_kymmscrybjfr743416j1qq4af FOREIGN KEY (creator_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT kpm_material_file_fk FOREIGN KEY (file_id_)
  REFERENCES public.wx_file (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT kpm_material_group_fk FOREIGN KEY (group_id_)
  REFERENCES public.wx_group (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.kpm_material
  OWNER TO postgres;

DROP TABLE IF EXISTS public.kpm_material_comment CASCADE;

CREATE TABLE public.kpm_material_comment
(
  id_          BIGINT,
  create_date_ TIMESTAMP WITH TIME ZONE,
  modify_date_ TIMESTAMP WITH TIME ZONE,
  material_id_ BIGINT,
  removed_     BOOLEAN DEFAULT FALSE,
  creator_id_  BIGINT,
  text         TEXT,
  CONSTRAINT kpm_material_comment_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_kpm_material_comment_kpm_material FOREIGN KEY (material_id_)
  REFERENCES public.kpm_material (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_kpm_material_comment_wx_user_creator FOREIGN KEY (creator_id_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.kpm_material_comment
  OWNER TO postgres;

DROP TABLE IF EXISTS public.kpm_material_fact CASCADE;

CREATE TABLE public.kpm_material_fact
(
  id_           BIGINT  NOT NULL,
  create_date_  TIMESTAMP WITH TIME ZONE,
  modify_date_  TIMESTAMP WITH TIME ZONE,
  url_          TEXT,
  physical_     TEXT,
  comment_      TEXT,
  creator_id_   BIGINT,
  removed_      BOOLEAN NOT NULL DEFAULT FALSE,
  material_id_  BIGINT,
  name_ru_      TEXT,
  platform_key_ TEXT,
  CONSTRAINT kpm_material_fact_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_kpm_material_fact_kpm_material_material FOREIGN KEY (material_id_)
  REFERENCES public.kpm_material (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_kpm_material_fact_wx_user_creator FOREIGN KEY (creator_id_)
  REFERENCES public.wx_user (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.kpm_material_fact
  OWNER TO postgres;

DROP TABLE IF EXISTS public.kpm_material_reaction CASCADE;

CREATE TABLE public.kpm_material_reaction
(
  id_               BIGINT  NOT NULL,
  create_date_      TIMESTAMP WITH TIME ZONE,
  modify_date_      TIMESTAMP WITH TIME ZONE,
  material_id_      BIGINT,
  date_             TIMESTAMP WITH TIME ZONE,
  platform_key_     TEXT,
  like_count_       BIGINT,
  dislike_count_    BIGINT,
  repost_count_     BIGINT,
  view_count_       BIGINT,
  comment_count_    BIGINT,
  removed_          BOOLEAN NOT NULL DEFAULT FALSE,
  material_fact_id_ BIGINT,
  CONSTRAINT kpm_material_reaction_pkey PRIMARY KEY (id_),
  CONSTRAINT fk_kpm_material_reaction_kpm_material FOREIGN KEY (material_id_)
  REFERENCES public.kpm_material (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_kpm_material_reaction_kpm_material_fact FOREIGN KEY (material_fact_id_)
  REFERENCES public.kpm_material_fact (id_) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.kpm_material_reaction
  OWNER TO postgres;

DROP TABLE IF EXISTS public.wx_notification CASCADE;

CREATE TABLE public.wx_notification
(
  id_              BIGINT  NOT NULL,
  create_date_     TIMESTAMP WITH TIME ZONE,
  modify_date_     TIMESTAMP WITH TIME ZONE,
  type_            TEXT,
  container_id_    BIGINT,
  container_class_ TEXT,
  removed_         BOOLEAN NOT NULL DEFAULT FALSE,
  start_date_      TIMESTAMP WITH TIME ZONE,
  status_          TEXT,
  CONSTRAINT pk_wx_notification PRIMARY KEY (id_)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.wx_notification
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.generic_stamp()
  RETURNS TRIGGER AS
$BODY$
BEGIN
  IF (TG_OP = 'INSERT')
  THEN
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

DROP TRIGGER IF EXISTS wx_notification_timestamp
ON public.wx_notification;

CREATE TRIGGER wx_notification_timestamp
BEFORE INSERT OR UPDATE
  ON public.wx_notification
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS wx_user_timestamp
ON public.wx_user;

CREATE TRIGGER wx_user_timestamp
BEFORE INSERT OR UPDATE
  ON public.wx_user
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS wx_group_timestamp
ON public.wx_group;

CREATE TRIGGER wx_group_timestamp
BEFORE INSERT OR UPDATE
  ON public.wx_group
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS wx_field_timestamp
ON public.wx_field;

CREATE TRIGGER wx_field_timestamp
BEFORE INSERT OR UPDATE
  ON public.wx_field
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS kpm_subscription_timestamp
ON public.kpm_subscription;

CREATE TRIGGER kpm_subscription_timestamp
BEFORE INSERT OR UPDATE
  ON public.kpm_subscription
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS kpm_material_fact_timestamp
ON public.kpm_material_fact;

CREATE TRIGGER kpm_material_fact_timestamp
BEFORE INSERT OR UPDATE
  ON public.kpm_material_fact
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS kpm_material_comment_timestamp
ON public.kpm_material_comment;

CREATE TRIGGER kpm_material_comment_timestamp
BEFORE INSERT OR UPDATE
  ON public.kpm_material_comment
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

DROP TRIGGER IF EXISTS kpm_material_timestamp
ON public.kpm_material;

CREATE TRIGGER kpm_material_timestamp
BEFORE INSERT OR UPDATE
  ON public.kpm_material
FOR EACH ROW
EXECUTE PROCEDURE public.generic_stamp();

CREATE SEQUENCE public.kpm_sequence
   INCREMENT 1
   START 1
   MINVALUE 1
   MAXVALUE 999999999999999999
   CACHE 1;

CREATE SEQUENCE public.history_sequence
   INCREMENT 1
   START 1
   MINVALUE 1
   MAXVALUE 999999999999999999
   CACHE 1;
