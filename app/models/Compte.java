package models;

import org.apache.commons.codec.digest.DigestUtils;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "compte")
public class Compte extends Model {

    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = true)
    public String nom;

    @Column(nullable = true)
    public String prenom;

    @Column(nullable = true)
    public String motDePasse;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    public Groupe groupe;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    public Compte(String email, String nom, String prenom, String motDePasse) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = DigestUtils.sha512Hex(motDePasse);
        this.groupe = Groupe.MEMBRE;
        this.dateCreation = new Date();
    }

    public enum Groupe {
        SUPER_ADMINISTRATEUR("SUPER ADMINISTRATEUR"),
        ADMINISTRATEUR("ADMINISTRATEUR"),
        MEMBRE("MEMBRE");

        public String traduction;

        Groupe(String traduction) {
            this.traduction = traduction;
        }
    }
}
