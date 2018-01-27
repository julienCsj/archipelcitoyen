# --- !Ups
create table cercle(
  id int8,
  nom text,
  slug text,
  nomCoordinateur varchar(255),
  mailCoordinateur varchar(255),
  telCoordinateur varchar(255),
  description text,
  contenuPage text,
  lienMattermost text,
  mailingList text,
  dateCreation timestamp,
  afficherFront boolean default false,
  logo_id int8,
  couverture_id int8,
  primary key (id)
);

create table fichier(
  id int8,
  url text,
  name varchar(255),
  dateCreation timestamp,
  cercle_id int8,
  compte_id int8,
  article_id int8,
  primary key (id)
);

create table article(
  id int8,
  titre text,
  introduction text,
  slug text,
  contenu text,
  dateCreation timestamp,
  afficherFront boolean default false,
  couverture_id int8,
  cercle_id int8,
  compte_id int8,
  primary key (id)
);

create table pressbook(
  id int8,
  titre text,
  description text,
  lienArticle text,
  logo_id int8,
  fichierArticle_id int8,
  datePublication timestamp,
  primary key (id)
);

create table slogan(
  id int8,
  texte text,
  primary key (id)
);

alter table fichier add CONSTRAINT fk_fichier_cercle_id FOREIGN KEY (cercle_id) REFERENCES cercle (id);
alter table fichier add CONSTRAINT fk_fichier_compte_id FOREIGN KEY (compte_id) REFERENCES compte (id);
alter table fichier add CONSTRAINT fk_fichier_article_id FOREIGN KEY (article_id) REFERENCES article (id);

alter TABLE cercle add CONSTRAINT fk_cercle_logo_id FOREIGN KEY (logo_id) REFERENCES fichier(id);
alter TABLE cercle add CONSTRAINT fk_cercle_couverture_id FOREIGN KEY (couverture_id) REFERENCES fichier(id);

alter TABLE article add CONSTRAINT fk_article_couverture_id FOREIGN KEY (couverture_id) REFERENCES fichier(id);
alter TABLE article add CONSTRAINT fk_article_cercle_id FOREIGN KEY (cercle_id) REFERENCES cercle(id);
alter TABLE article add CONSTRAINT fk_article_compte_id FOREIGN KEY (compte_id) REFERENCES compte(id);

alter TABLE pressbook add CONSTRAINT fk_pressbook_logo_id FOREIGN KEY (logo_id) REFERENCES fichier(id);
alter TABLE pressbook add CONSTRAINT fk_pressbook_fichierArticle_id FOREIGN KEY (fichierArticle_id) REFERENCES fichier(id);


# --- !Downs
drop table article CASCADE ;
drop table cercle CASCADE ;
drop table pressbook CASCADE ;
drop table fichier;
drop table slogan;

