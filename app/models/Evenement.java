package models;

import org.hibernate.annotations.Type;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "evenement")
public class Evenement extends Model {

    @Column(unique = false, nullable = false)
    public String titre;

    @Column(unique = false, nullable = false, length = 5000)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String description;

    @Column(unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateDebut;

    @Column(unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateFin;

    @Column(unique = false, nullable = false)
    public String lieu;

    @Column(unique = false)
    public String urlImage;

    @Column
    public Boolean enAvant;

    @Column(unique = false)
    public String lat;

    @Column(unique = false)
    public String lon;

    public Evenement() {
        this.enAvant = false;
    }
}
