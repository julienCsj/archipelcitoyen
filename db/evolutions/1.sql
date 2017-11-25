# --- !Ups

create sequence hibernate_sequence
;

create table compte
(
  id bigint not null
    constraint compte_pkey
    primary key,
  datecreation timestamp,
  email varchar(255) not null
    constraint uk_55pqxvdmn5q8piy3nc1b3lr69
    unique,
  groupe varchar(255),
  motdepasse varchar(255),
  nom varchar(255),
  prenom varchar(255)
)
;

create table evenement
(
  id bigint not null
    constraint evenement_pkey
    primary key,
  datedebut timestamp not null,
  datefin timestamp not null,
  description text not null,
  enavant boolean,
  lieu varchar(255) not null,
  titre varchar(255) not null,
  urlimage varchar(255) not null
)
;

create table fragment
(
  id bigint not null
    constraint fragment_pkey
    primary key,
  contenu text not null,
  description varchar(255) not null,
  nom varchar(255) not null
)
;

create table page_front
(
  id bigint not null
    constraint page_front_pkey
    primary key,
  contenu text not null,
  slug varchar(255) not null,
  titre varchar(255) not null
)
;

create table slide_accueil
(
  id bigint not null
    constraint slide_accueil_pkey
    primary key,
  soustitre varchar(255),
  textebouton varchar(255),
  titre varchar(255),
  urlbouton varchar(255),
  urlimage text not null
)
;

# --- !Downs
drop sequence hibernate_sequence;
drop table compte;
drop table evenement;
drop table fragment;
drop table page_front;
drop table slide_accueil;
