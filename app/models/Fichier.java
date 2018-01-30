package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fichier")
public class Fichier extends Model {

    @Column(unique = true, nullable = false)
    public String url;

    @Column
    public String name;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Cercle cercle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Compte compte;

    @Column
    public Boolean afficherFront;


    public Fichier() {
        this.afficherFront = false;
        this.dateCreation = new Date();
    }

}
