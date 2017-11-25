package models;

import org.hibernate.annotations.Type;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "fragment")
public class Fragment extends Model {

    @Column(unique = false, nullable = false)
    public String nom;

    @Column(unique = false, nullable = false)
    public String description;

    @Column(unique = false, nullable = false)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String contenu;
}
