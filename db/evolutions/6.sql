# --- !Ups

create table adherent(
id int8,
civilite varchar(255),
nom varchar(255),
prenom varchar(255),
adresse text(255),
ville varchar(255),
codePostal varchar(255),
pays varchar(255),
dateCreation timestamp,
adhesionPayee boolean default false,
email varchar(255),
telephone varchar(255),
mandat text(255),
primary key(id)
);

# --- !Downs
drop table adherent;
