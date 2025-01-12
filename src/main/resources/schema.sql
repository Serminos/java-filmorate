CREATE TABLE IF NOT EXISTS rating_mpa (
	rating_mpa_id bigserial NOT NULL,
	name varchar NOT NULL,
	description varchar NULL,
	CONSTRAINT rating_mpa_pk PRIMARY KEY (rating_mpa_id)
);

CREATE TABLE IF NOT EXISTS film (
	film_id bigserial NOT NULL,
	name varchar NOT NULL,
	description varchar(200) NULL,
	release_date date NULL,
	duration int8 NULL,
	rating_mpa_id int8 NULL,
	CONSTRAINT film_pk PRIMARY KEY (film_id),
	CONSTRAINT film_rating_mpa_fk FOREIGN KEY (rating_mpa_id) REFERENCES rating_mpa(rating_mpa_id)
);

CREATE TABLE IF NOT EXISTS users (
	user_id bigserial NOT NULL,
	email varchar NOT NULL,
	login varchar NOT NULL,
	name varchar NULL,
	birthday date NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT users_unique UNIQUE (email)
);


CREATE TABLE IF NOT EXISTS genre (
	genre_id bigserial NOT NULL,
	name varchar NOT NULL,
	CONSTRAINT genre_pk PRIMARY KEY (genre_id),
	CONSTRAINT genre_unique UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id int8 NOT NULL,
	genre_id int8 NOT NULL,
	CONSTRAINT film_genre_unique UNIQUE (film_id, genre_id),
	CONSTRAINT film_genre_film_fk FOREIGN KEY (film_id) REFERENCES film(film_id),
	CONSTRAINT film_genre_genre_fk FOREIGN KEY (genre_id) REFERENCES genre(genre_id)
);

CREATE TABLE IF NOT EXISTS friendship (
	from_user_id int8 NOT NULL,
	to_user_id int8 NOT NULL,
	is_confirmed bool NULL,
	CONSTRAINT friendship_unique UNIQUE (from_user_id, to_user_id),
	CONSTRAINT friendship_user_fk FOREIGN KEY (from_user_id) REFERENCES users(user_id),
	CONSTRAINT friendship_user_fk_1 FOREIGN KEY (to_user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS film_user_like (
	film_id int8 NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT film_user_like_film_fk FOREIGN KEY (film_id) REFERENCES film(film_id),
	CONSTRAINT film_user_like_user_fk FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS review (
                                     review_id bigserial NOT NULL,
                                     content varchar NOT NULL,
                                     is_positive boolean NOT NULL,
                                     user_id int8 NOT NULL,
                                     film_id int8 NOT NULL,
    CONSTRAINT review_pk PRIMARY KEY (review_id),
    CONSTRAINT review_user_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT review_film_fk FOREIGN KEY (film_id) REFERENCES film(film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review_like (
                                           review_id int8 NOT NULL,
                                           user_id int8 NOT NULL,
                                           is_like bool NULL,
    CONSTRAINT review_like_unique UNIQUE (review_id, user_id),
    CONSTRAINT review_like_review_fk FOREIGN KEY (review_id) REFERENCES review(review_id) ON DELETE CASCADE,
    CONSTRAINT review_like_user_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    );