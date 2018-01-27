package models;

import play.db.jpa.Model;
import play.mvc.Router;
import play.templates.JavaExtensions;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "slogan")
public class Slogan extends Model {

    @Column(unique = true, nullable = false)
    public String texte;

    public Slogan() {
    }
}
