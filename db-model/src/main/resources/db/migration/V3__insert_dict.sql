truncate table wx_field;
truncate table wx_dictionary;

insert into wx_dictionary (code_, name_ru_, name_kk_, name_en_) values ('categories', 'Категории', 'Категории', 'Категории');
insert into wx_dictionary (code_, name_ru_, name_kk_, name_en_) values ('marks', 'Метки', 'Метки', 'Метки');
insert into wx_dictionary (code_, name_ru_, name_kk_, name_en_) values ('languages', 'Языки', 'Языки', 'Языки');
insert into wx_dictionary (code_, name_ru_, name_kk_, name_en_) values ('ages', 'Возрастные категории', 'Возрастные категории', 'Возрастные категории');
insert into wx_dictionary (code_, name_ru_, name_kk_, name_en_) values ('involvements', 'Вовлеченность', 'Вовлеченность', 'Вовлеченность');

--categories
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('categories','0','Без категории','Без категории','Without category',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('categories','1','Поездка в Сирию','Поездка в Сирию','Trip to Syria',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('categories','2','ЗОЖ','ЗОЖ','ZOJ',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('categories','3','Антитеррор','Антитеррор','Antiterror',TRUE,FALSE, current_timestamp, current_timestamp);

--marks
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('marks','1','Детское','Детское','Child',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('marks','2','ЗОЖ','ЗОЖ','ZOJ',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('marks','3','Курение','Курение','Smoking',TRUE,FALSE, current_timestamp, current_timestamp);

--languages
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('languages','ru','Русский','Русский','Russian',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('languages','kk','Казахский','Казахский','Kazakh',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('languages','en','Английский','Английский','English',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('languages','ar','Арабский','Арабский','Arabic',TRUE,FALSE, current_timestamp, current_timestamp);

--ages
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('ages','1','15-19','15-19','15-19',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('ages','2','20-25','20-25','20-25',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('ages','3','26-31','26-31','26-31',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('ages','4','31 и выше','31 и выше','31 и выше',TRUE,FALSE, current_timestamp, current_timestamp);

--involvements
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('involvements','1','Ознакомление','Ознакомление','Ознакомление',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('involvements','2','Заинтересованность','Заинтересованность','Заинтересованность',TRUE,FALSE, current_timestamp, current_timestamp);
insert into wx_field(dictionary_code_, key_, ru_, kk_, en_, enabled_, removed_, create_date_, modify_date_) values ('involvements','3','Фанатизм','Фанатизм','Фанатизм',TRUE,FALSE, current_timestamp, current_timestamp);
