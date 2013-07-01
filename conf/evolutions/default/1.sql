# --- First database schema

# --- !Ups

create table bar (
  id    bigint AUTO_INCREMENT PRIMARY KEY,
  name  varchar(128)
);


# --- !Downs

drop table bar;