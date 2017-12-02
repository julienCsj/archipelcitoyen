# --- !Ups
alter table evenement add column lat varchar(255);
alter table evenement add column lon varchar(255);

# --- !Downs
alter table evenement dropd column lat;
alter table evenement dropd column lon;
