# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bar (
  name                      varchar(255))
;




# --- !Downs

drop table if exists bar cascade;

