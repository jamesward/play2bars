# --- First database schema

# --- !Ups

create table bar (
    id    bigint not null primary key auto_increment,
    name  varchar(128)
);

# --- !Downs

drop table if exists bar;