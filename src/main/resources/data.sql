MERGE INTO GENRE (GENRE_ID, NAME) VALUES(1, 'Комедия');
MERGE INTO GENRE (GENRE_ID, NAME) VALUES(2, 'Драма');
MERGE INTO GENRE (GENRE_ID, NAME) VALUES(3, 'Мультфильм');
MERGE INTO GENRE (GENRE_ID, NAME) VALUES(4, 'Триллер');
MERGE INTO GENRE (GENRE_ID, NAME) VALUES(5, 'Документальный');
MERGE INTO GENRE (GENRE_ID, NAME) VALUES(6, 'Боевик');

MERGE INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES(1, 'G', 'Нет возрастных ограничений');
MERGE INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES(2, 'PG', 'Детям рекомендуется смотреть фильм с родителями');
MERGE INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES(3, 'PG-13', 'Детям до 13 лет просмотр не желателен');
MERGE INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES(4, 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого');
MERGE INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES(5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');




