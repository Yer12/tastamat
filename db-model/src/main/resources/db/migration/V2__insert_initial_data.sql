-- insert into wx_user
--
--
-- admin@kontr.kz

INSERT INTO public.wx_group(
            create_date_, modify_date_, enabled_, removed_, name_kk_, name_ru_,
            name_en_, id_)
    VALUES (now(), now(), true, false, 'adm', 'adm',
            'adm', 1);

INSERT INTO public.wx_user(
            id_, email_, create_date_, modify_date_, enabled_, removed_,
            lastname_, firstname_, patronymic_, status_, creator_, role_,
            branch_, mobile_phone_, phone_, additional_, default_language_,
            photo_)
    VALUES (nextval('kpm_sequence'), 'admin@kontr.kz', now(), now(), true, false,
            'admin', 'admin', 'admin', 'ACTIVE', null, 'ADMIN',
            1, null, null, null, 'ru', null);

select nextval('kpm_sequence');
