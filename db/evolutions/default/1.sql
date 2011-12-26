# --- First database schema

# --- !Ups

create table bar (
  id                        bigint not null primary key,
  name                      varchar(255) not null,
  location                  varchar(255)
);

create sequence bar_seq start with 1000;


# --- !Downs

drop table if exists bar;
drop sequence if exists bar_seq;