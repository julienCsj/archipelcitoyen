package models;

import play.db.jpa.Model;
import play.mvc.Router;
import play.templates.JavaExtensions;
import services.aws.S3FileUploadService;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pressbook")
public class PressBook extends Model {

    @Column(unique = true, nullable = false)
    public String titre;

    @Column(nullable = true)
    public String description;

    @Column(nullable = true)
    public String lienArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Fichier logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = true, nullable = true)
    public Fichier fichierArticle;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date datePublication;

    public PressBook delete() {
        Fichier logo = this.logo;
        Fichier article = this.fichierArticle;
        super.delete();
        S3FileUploadService.deleteFile(logo);
        S3FileUploadService.deleteFile(article);
        logo.delete();
        article.delete();
        return this;
    }

    public PressBook() {
    }
}
