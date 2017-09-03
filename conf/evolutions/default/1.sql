# --- !Ups

CREATE TABLE bar (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS bar;

