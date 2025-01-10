# Filmorate

## Назначение

Filmorate — это приложение аналог кинопоиска:)

## ER-диаграмма базы данных

![Схема базы данных Filmorate](filmorate.png)

## Описание схемы

На данной диаграмме представлены следующие таблицы:
- **film** 
Содержит информацию о фильмах(название фильма, описание, дату выпуска и продолжительность фильма).
Таблица включает поля:
  первичный ключ film_id — идентификатор фильма
  name — название фильма
  description — описание фильма
  release_date — дата выхода фильма
  duration — длительность фильма
  внешний ключ rating_mpa_id — — идентификатор рейтинга по версии MPA
- **film_genre** 
Содержит информацию о жанрах фильмов из таблицы film.
В таблицу входят поля:
   первичный ключ film_id — идентификатор фильма
   внешний ключ genre_id — идентификатор жанра
- **genre**
Содержит информацию о жанрах фильмов, связанных с фильмами через таблицу `film_genre`.
В таблицу входят поля:
  первичный ключ genre_id — идентификатор жанра
  name — наименование жанра
- **rating_mpa** 
Содержит информацию о рейтинге MPA.
В таблицу входят поля:
  первичный ключ rating_mpa_id — идентификатор рейтинга
  "name" — название рейтинга
  description — описание рейтинга
- **film_user_like**
Содержит информацию о связи между фильмами и пользователями поставившие лайк.
В таблицу входят поля:
  внешний ключ film_id — идентификатор фильма
  внешний ключ user_id — идентификатор пользователя
- **user** 
Содержит информацию о пользователях.
В таблицу входят поля:
  первичный ключ user_id — идентификатор пользователя
  email — email пользователя
  login — логин пользователя
  "name" — имя пользователя
  birthday — день рождения пользователя
- **friendship**
Содержит информацию о дружбе между пользователями.
В таблицу входят поля:
  from_user_id — идентификатор пользователя отправившего запрос на дружбу
  to_user_id — идентификатор пользователя кому отправлен запрос на дружбу
  is_confirmed — подтверждена да/нет


## Примеры SQL-запросов

### Добавления рейтинга MPA:
```sql
INSERT INTO rating_mpa
(rating_mpa_id, "name", description)
VALUES(0, 'G', 'у фильма нет возрастных ограничений');
```
### Добавления жанра:
```sql
INSERT INTO public.genre
(genre_id, "name")
VALUES(nextval('genre_genre_id_seq'::regclass), 'драма');
INSERT INTO public.genre
(genre_id, "name")
VALUES(nextval('genre_genre_id_seq'::regclass), 'семейный');
```
### Добавления жанра для фильма:
```sql
INSERT INTO public.film_genre
(film_id, genre_id)
VALUES(1, 1);
INSERT INTO public.film_genre
(film_id, genre_id)
VALUES(1, 2);
```
### Добавления фильма:
```sql
INSERT INTO film
(film_id, "name", description, release_date, duration, rating_mpa_id)
VALUES(nextval('film_film_id_seq'::regclass), 'Чайка по имени Джонатан Ливингстон', 
'Джонатану опротивела скучная жизнь его клана чаек. Он был с головой погружен в смелые эксперименты с техникой полетов и жаждал высотных далей. И потому что он был не такой как все..', 
'1973-10-23', 99, 0);
```
## Пример добавления пользователя:
```sql 
INSERT INTO users
(user_id, email, login, "name", birthday)
VALUES(nextval('users_user_id_seq'::regclass), 'Иван Иванов', 'ivaniI', 'ivaniI@test.ru', '1973-10-23');
```
## Пример добавления лайка к фильму
```sql
INSERT INTO film_user_like
(film_id, user_id)
VALUES(1, 1);
```
## Пример сохранения запроса на дружбу (пользователь 1 отправил запрос пользователю 2)
```sql
INSERT INTO public.friendship
(from_user_id, to_user_id, is_confirmed)
VALUES(1, 2, false);
```
## Пример получения популярных Топ-10 фильмов по числу лайков
```sql
SELECT f.name, COUNT(ful.user_id) AS likes
FROM film f
LEFT JOIN film_user_like ful ON f.film_id = ful.film_id
GROUP BY f.film_id
ORDER BY likes DESC
LIMIT 10;
```
## Пример получения популярных Топ-10 фильмов по числу лайков
```sql
SELECT f.name, COUNT(ful.user_id) AS likes
FROM film f
LEFT JOIN film_user_like ful ON f.film_id = ful.film_id
GROUP BY f.film_id
ORDER BY likes DESC
LIMIT 10;
```
##   Пример получения подтвержденных друзей пользователя
```sql
SELECT u.user_id, u.name
FROM friendship f
JOIN users u on f.to_user_id = u.user_id
WHERE f.from_user_id = 1
AND f.is_confirmed = true ;