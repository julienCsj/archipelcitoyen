package models;

import org.hibernate.annotations.Type;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "slide_accueil")
public class SlideAccueil extends Model {

    @Column(unique = false, nullable = false)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String urlImage;

    @Column(unique = false, nullable = true)
    public String titre;

    @Column(unique = false, nullable = true)
    public String sousTitre;

    @Column(unique = false, nullable = true)
    public String texteBouton;

    @Column(unique = false, nullable = true)
    public String urlBouton;
}
