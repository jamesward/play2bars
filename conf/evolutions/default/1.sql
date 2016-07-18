# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bar (
  id                            uuid not null,
  name                          varchar(255),
  constraint pk_bar primary key (id)
);


# --- !Downs

drop table if exists bar cascade;

