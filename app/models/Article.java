package models;

import play.db.jpa.Model;
import play.mvc.Router;
import play.templates.JavaExtensions;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "article")
public class Article extends Model {

    @Column(unique = true, nullable = false)
    public String titre;

    @Column(nullable = true)
    public String slug;

    @Column(nullable = true)
    public String contenu;

    @Column(nullable = true)
    public String introduction;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    @Column
    public boolean afficherFront;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Fichier couverture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Cercle cercle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Compte compte;


    public String getUrl() {
        return Router.getBaseUrl() + "/" + this.slug;
    }

    public Article() {
        this.dateCreation = new Date();
        this.afficherFront = false;
    }

    public void setSlug() {
        this.slug = JavaExtensions.slugify(this.titre);
    }
}
