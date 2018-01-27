package models;

import org.apache.commons.codec.digest.DigestUtils;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cercle")
public class Cercle extends Model {

    @Column(unique = true, nullable = false)
    public String nom;

    @Column(unique = true, nullable = false)
    public String slug;

    @Column(nullable = true)
    public String nomCoordinateur;

    @Column(nullable = true)
    public String mailCoordinateur;

    @Column(nullable = true)
    public String telCoordinateur;

    @Column(nullable = true)
    public String description;

    @Column(nullable = true)
    public String contenuPage;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    @Column
    public boolean afficherFront;

    @Column
    public String lienMattermost;

    @Column
    public String mailingList;

    @Column
    public String pictogramme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Fichier logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Fichier couverture;

    public Cercle() {
        this.dateCreation = new Date();
        this.afficherFront = false;
    }
}
