package models;

import org.hibernate.annotations.Type;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "page_front")
public class PagesFront extends Model {

    @Column(unique = false, nullable = false)
    public String slug;

    @Column(unique = false, nullable = false)
    public String titre;

    @Column(unique = false, nullable = false)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String contenu;
}
