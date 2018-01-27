# --- !Ups

alter table cercle add column pictogramme varchar(255);

# --- !Downs
alter table cercle drop column pictogramme;

