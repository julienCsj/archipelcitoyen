package controllers.backoffice;

import models.Article;
import models.Cercle;
import models.Compte;
import models.Fichier;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Before;
import play.templates.JavaExtensions;
import services.aws.S3FileUploadService;
import services.ovh.OVHConnector;
import services.ovh.RedirectionMail;
import utils.StringUtils;

import java.io.File;
import java.util.List;

public class Cercles extends SecureController {

    private static void setCompteurs(Long id) {
        Cercle cercle = Cercle.findById(id);
        renderArgs.put("nbDocuments", Fichier.count("cercle = ?", cercle));
        renderArgs.put("nbArticles", Article.count("cercle = ?", cercle));
    }



    public static void creerCercle() {
        render();
    }

    public static void creerCerclePost(@Required String nom) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            creerCercle();
        }

        Cercle cercle = new Cercle();
        cercle.nom = nom;
        cercle.slug = JavaExtensions.slugify(nom);
        cercle.save();

        flash.success("Le cercle est ajoutée");
        lireCerclePage(cercle.id);
    }


    public static void editerInformationsCercle(Long id) {
        Cercle cercle = Cercle.findById(id);
        render(cercle);
    }

    public static void editerInformationsCerclePost(@Required Long id, @Required String nom, String nomCoordinateur, String mailCoordinateur,
                                                    String telCoordinateur, boolean afficherFront, String mailingList, String lienMattermost,
                                                    File logo, File couverture, String pictogramme) {

        Compte compte = getCompte();

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            editerInformationsCercle(id);
        }

        Cercle cercle = Cercle.findById(id);
        if(cercle != null) {
            cercle.nom = nom;
            cercle.nomCoordinateur = nomCoordinateur;
            cercle.mailCoordinateur = mailCoordinateur;
            cercle.telCoordinateur = telCoordinateur;
            cercle.mailingList = mailingList;
            cercle.lienMattermost = lienMattermost;
            cercle.afficherFront = afficherFront;
            cercle.pictogramme = pictogramme;

            if(logo != null) {
                Fichier s3 = new S3FileUploadService(compte)
                        .dossier(S3FileUploadService.S3Dossier.DOCUMENTS_CERCLE)
                        .payload(logo)
                        .resize(128, 128)
                        .name("Logo cercle "+cercle.nom)
                        .upload();

                s3.save();
                if(s3 != null) {
                    cercle.logo = s3;
                }
            }

            if(couverture != null) {
                Fichier s3 = new S3FileUploadService(compte)
                        .dossier(S3FileUploadService.S3Dossier.DOCUMENTS_CERCLE)
                        .payload(couverture)
                        .name("Couverture cercle "+cercle.nom)
                        .upload();

                s3.save();
                if(s3 != null) {
                    cercle.couverture = s3;
                }
            }

            cercle.save();
            flash.success("Informations sauvegardées");
        } else {
            flash.error("Une erreur est survenue.");
        }

        lireCerclePage(id);
    }

    public static void lireCerclePage(Long id) {
        Cercle cercle = Cercle.findById(id);
        setCompteurs(id);
        render(cercle);
    }

    public static void lireCerclePageEditerPost(Long id, String description, String contenuPage) {
        Cercle cercle = Cercle.findById(id);
        cercle.description = description;
        cercle.contenuPage = contenuPage;
        cercle.save();
        lireCerclePage(id);
    }

    public static void lireCercleMailingLists(Long id) {
        Cercle cercle = Cercle.findById(id);
        setCompteurs(id);
        List<RedirectionMail> redirections = null;
        if(cercle.mailingList != null && !cercle.mailingList.isEmpty()) {
            redirections = OVHConnector.getRedirections(cercle.mailingList);

            for (RedirectionMail redirection : redirections) {
                System.out.println(redirection);
            }
        }

        render(cercle, redirections);
    }

    public static void lireCercleMailingListsInitPost(Long id, @Required String mailingList) {
        Cercle cercle = Cercle.findById(id);
        Compte compte = getCompte();

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            lireCercleMailingLists(id);
        }

        if(mailingList != null && !mailingList.isEmpty()) {
            OVHConnector.createNewRedirection(mailingList+"@larchipelcitoyen.org", compte.email);
            cercle.mailingList = mailingList+"@larchipelcitoyen.org";
            cercle.save();
            flash.success("Mailing list active, vous pouvez maintenant ajouter des emails");
        }

        lireCercleMailingLists(id);
    }

    public static void lireCercleMailingListsSupprimerRedirection(Long id, String idRedirection) {
        boolean res = OVHConnector.deleteEmailRedirection(idRedirection);
        if(res) flash.success("Mail supprimé de la mailing list");
        else flash.error("Une erreur est survenue");

        lireCercleMailingLists(id);
    }

    public static void lireCercleMailingListsAjouterPost(Long idCercle, String emails) {
        Cercle cercle = Cercle.findById(idCercle);
        int nbSucces = 0;
        int nbError = 0;

        List<String> emailsList = StringUtils.extractEmailsFromString(emails);
        for (String to : emailsList) {
            boolean res = OVHConnector.createNewRedirection(cercle.mailingList, to);
            if(res) nbSucces++; else nbError++;
        }

        if(nbSucces > 0) flash.success(nbSucces + " emails ajoutés à la mailing list "+cercle.mailingList);
        if(nbError > 0) flash.error(nbError + " emails n'ont pas été ajoutés à la mailing list "+cercle.mailingList);
        lireCercleMailingLists(idCercle);
    }

    public static void lireCercleArticles(Long id) {
        Cercle cercle = Cercle.findById(id);
        setCompteurs(id);

        List<Article> articles = Article.find("cercle = ? order by dateCreation DESC", cercle).fetch();
        render(cercle, articles);
    }

    public static void lireCercleArticlesAjouterPost(Long idCercle, String titre) {
        Cercle cercle = Cercle.findById(idCercle);
        Compte compte = getCompte();

        Article article = new Article();
        article.compte = compte;
        article.cercle = cercle;
        article.titre = titre;
        article.setSlug();
        article.save();

        lireCercleArticlesEditerArticle(idCercle, article.id);
    }

    public static void lireCercleArticlesEditerArticle(Long idCercle, Long idArticle) {
        Cercle cercle = Cercle.findById(idCercle);
        setCompteurs(idCercle);
        Article article = Article.findById(idArticle);
        List<Fichier> piecesJointes = Fichier.find("article = ?", article).fetch();
        render(cercle, article, piecesJointes);
    }

    public static void lireCercleArticlesEditerArticlePost(Long idCercle, Long idArticle, String titre, String introduction, String contenu, boolean afficherFront, File couverture, String nom, File fichier) {
        Cercle cercle = Cercle.findById(idCercle);
        Article article = Article.findById(idArticle);
        Compte compte = getCompte();

        article.titre = titre;
        article.setSlug();
        article.contenu = contenu;
        article.introduction = introduction;
        article.cercle = cercle;
        article.afficherFront = afficherFront;

        if(couverture != null) {
            Fichier s3 = new S3FileUploadService(compte)
                    .dossier(S3FileUploadService.S3Dossier.DOCUMENTS_CERCLE)
                    .payload(couverture)
                    .name("Couverture article "+article.titre)
                    .upload();
            if(s3 != null) {
                s3.save();
                article.couverture = s3;
            }
        }

        if(nom != null && fichier != null) {
            Fichier fichierSurS3 = new S3FileUploadService(compte)
                    .name(nom)
                    .payload(fichier)
                    .dossier(S3FileUploadService.S3Dossier.PJ_ARTICLE)
                    .upload();

            if(fichierSurS3 != null) {
                fichierSurS3.article = article;
                fichierSurS3.save();
            }
        }

        article.save();
        flash.success("Article sauvegardé");
        lireCercleArticlesEditerArticle(idCercle, idArticle);
    }

    public static void lireCercleArticlesSupprimerArticle(Long idCercle, Long idArticle) {
        Article article = Article.findById(idArticle);
        if(article.couverture != null) article.couverture.delete();
        article.delete();
        lireCercleArticles(idCercle);
    }

    public static void lireCercleDocuments(Long id) {
        Cercle cercle = Cercle.findById(id);
        setCompteurs(id);
        List<Fichier> fichiers = Fichier.find("cercle = ?", cercle).fetch();
        render(cercle, fichiers);
    }

    public static void lireCercleDocumentsAjouterPost(Long idCercle, String nom, File fichier, boolean afficherFront) {
        Cercle cercle = Cercle.findById(idCercle);
        Compte compte = getCompte();
        Fichier fichierSurS3 = new S3FileUploadService(compte)
                .name(nom)
                .payload(fichier)
                .dossier(S3FileUploadService.S3Dossier.DOCUMENTS_CERCLE)
                .upload();

        fichierSurS3.cercle = cercle;
        fichierSurS3.afficherFront = afficherFront;

        if(fichierSurS3 != null) {
            fichierSurS3.save();
            flash.success("Document enregistré");
        } else {
            flash.error("Une erreur est survenue lors de l'envoi du fichier sur le serveur");
        }
        lireCercleDocuments(cercle.id);
    }

    public static void lireCercleDocumentsSupprimerDoc(Long id, Long idFichier) {
        Cercle cercle = Cercle.findById(id);
        Fichier fichier = Fichier.findById(idFichier);
        S3FileUploadService.deleteFile(fichier);
        fichier.delete();
        flash.success("Fichier supprimé");
        lireCercleDocuments(cercle.id);
    }

    public static void toogleAffichageFront(Long id) {
        Cercle cercle = Cercle.findById(id);
        cercle.afficherFront = !cercle.afficherFront;
        cercle.save();
        lireCerclePage(id);
    }
}
