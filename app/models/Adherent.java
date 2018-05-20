package models;

import play.db.jpa.Model;
import play.mvc.Router;
import play.templates.JavaExtensions;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adherent")
public class Adherent extends Model {

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    public Civilite civilite;

    @Column
    public String nom;

    @Column
    public String prenom;

    @Column
    public String mandat;

    @Column
    public String adresse;

    @Column
    public String codePostal;

    @Column
    public String ville;

    @Column
    public String pays;

    @Column
    public String email;

    @Column
    public String telephone;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    @Column
    public boolean adhesionPayee;

    public Adherent() {
        this.dateCreation = new Date();
        this.adhesionPayee = false;
    }

    public enum Civilite {
        MONSIEUR("Monsieur", "M."),
        MADAME("Madame", "Mme.");

        public String traductionLongue;
        public String traductionCourte;

        Groupe(String traductionL, String traductionC) {
            this.traductionLongue = traductionLongue;
            this.traductionCourte = traductionC;
        }
    }
}
