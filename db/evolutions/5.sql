# --- !Ups

alter table fichier add column afficherFront boolean default false;

# --- !Downs
alter table fichier drop column afficherFront;

