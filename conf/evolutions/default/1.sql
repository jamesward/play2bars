# --- First database schema

# --- !Ups

create sequence s_bar_id;

create table bar (
  id    bigint DEFAULT nextval('s_bar_id'),
  name  varchar(128)
);


# --- !Downs

drop table bar;
drop sequence s_bar_id;