# --- First database schema

# --- !Ups

create table bar (
  id                        SERIAL PRIMARY KEY,
  name                      varchar(255) not null
);

# --- !Downs

drop table if exists bar;
