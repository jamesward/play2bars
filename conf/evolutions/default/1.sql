# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bar (
  id                        varchar(255) not null,
  name                      varchar(255),
  constraint pk_bar primary key (id))
;

create sequence bar_seq;




# --- !Downs

drop table if exists bar cascade;

drop sequence if exists bar_seq;

