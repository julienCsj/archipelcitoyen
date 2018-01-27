package controllers.backoffice;

import models.Compte;
import models.Fichier;
import models.PressBook;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.templates.JavaExtensions;
import services.aws.S3FileUploadService;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Pressbooks extends SecureController {

    public static void index() {
        List<PressBook> pressbooks = PressBook.find("").fetch();
        render(pressbooks);
    }

    public static void ajouter() {
        render();
    }

    public static void ajouterPost(@Required String titre, String description, String lienArticle, File logo, File fichierArticle, @As("dd/MM/yyyy HH:mm") Date datePublication) {

        Compte compte = getCompte();

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            ajouter();
        }

        PressBook pressbook = new PressBook();
        pressbook.titre = titre;
        pressbook.description = description;
        pressbook.lienArticle = lienArticle;
        pressbook.datePublication = datePublication;

        if(fichierArticle != null) {
            Fichier upload = new S3FileUploadService(compte)
                    .dossier(S3FileUploadService.S3Dossier.PRESSBOOK)
                    .name("Article " + titre)
                    .payload(fichierArticle)
                    .upload();

            if(upload != null) {
                upload.save();
                pressbook.fichierArticle = upload;
            }
        }

        if(logo != null) {
            Fichier upload = new S3FileUploadService(compte)
                    .dossier(S3FileUploadService.S3Dossier.PRESSBOOK)
                    .name("Logo " + titre)
                    .payload(logo)
                    .resize(128, 128)
                    .upload();

            if(upload != null) {
                upload.save();
                pressbook.logo = upload;
            }
        }

        pressbook.save();

        flash.success("L'article de presse est ajouté");
        index();
    }

    public static void supprimer(Long id) {
        if(id != null) {
            PressBook.delete("id = ?", id);
            flash.success("Article de presse supprimé");
            index();
        }

        index();
    }


    public static void editer(Long id) {
        PressBook pressbook = PressBook.findById(id);
        render(pressbook);
    }

    public static void editerPost(@Required Long id, @Required String titre, String description, String lienArticle, File logo, File fichierArticle, @As("dd/MM/yyyy HH:mm") Date datePublication) {

        Compte compte = getCompte();

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            editer(id);
        }

        PressBook pressbook = PressBook.findById(id);
        if(pressbook != null) {
            pressbook.titre = titre;
            pressbook.description = description;
            pressbook.lienArticle = lienArticle;
            pressbook.datePublication = datePublication;

            if(fichierArticle != null) {
                Fichier upload = new S3FileUploadService(compte)
                        .dossier(S3FileUploadService.S3Dossier.PRESSBOOK)
                        .name("Article " + titre)
                        .payload(fichierArticle)
                        .upload();

                if(upload != null) {
                    upload.save();
                    pressbook.fichierArticle = upload;
                }
            }

            if(logo != null) {
                Fichier upload = new S3FileUploadService(compte)
                        .dossier(S3FileUploadService.S3Dossier.PRESSBOOK)
                        .name("Logo " + titre)
                        .payload(logo)
                        .resize(128,128)
                        .upload();

                if(upload != null) {
                    upload.save();
                    pressbook.logo = upload;
                }
            }

            pressbook.save();

            flash.success("Article de presse ajouté");
        } else {
            flash.error("Une erreur est survenue.");
        }

        index();
    }
}
