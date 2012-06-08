# --- First database schema

# --- !Ups

create table bar (
  id serial primary key,
  name varchar(128)
);

create sequence s_bar_id;

# --- !Downs

drop table bar;
drop sequence s_bar_id;