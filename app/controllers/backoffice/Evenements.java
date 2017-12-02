package controllers.backoffice;

import models.Evenement;
import models.Evenement;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.jpa.JPABase;
import play.templates.JavaExtensions;

import java.util.Date;
import java.util.List;

public class Evenements extends SecureController {

    public static void index() {
        List<Evenement> evenements = Evenement.find("order by dateDebut DESC").fetch();
        render(evenements);
    }

    public static void ajouter() {
        render();
    }

    public static void ajouterPost(String urlImage, @Required String titre, String description, @As("dd/MM/yyyy HH:mm") @Required Date dateDebut, @As("dd/MM/yyyy HH:mm") Date dateFin, String lieu, String lat, String lon) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            ajouter();
        }

        Evenement evenement = new Evenement();
        evenement.urlImage = urlImage;
        evenement.titre = titre;
        evenement.description = description;
        evenement.dateDebut = dateDebut;
        evenement.dateFin = dateFin;
        evenement.lieu = lieu;
        evenement.lat = lat;
        evenement.lon = lon;
        evenement.save();

        flash.success("L'évènement est ajouté");
        index();
    }

    public static void supprimer(Long id) {
        if(id != null) {
            Evenement.delete("id = ?", id);
            flash.success("evenement supprimée");
            index();
        }

        index();
    }

    public static void mettreEnAvant(Long id) {
        if(id != null) {

            List<Evenement> evenements = Evenement.find("enAvant = true").fetch();
            if(evenements != null && !evenements.isEmpty()) {
                for (Evenement evenement : evenements) {
                    evenement.enAvant = false;
                    evenement.save();
                }
            }

            Evenement evenement = Evenement.findById(id);
            evenement.enAvant = true;
            evenement.save();
            flash.success("Cet événement est maintenant mis en avant sur la page d'accueil du site");
            index();
        }

        index();
    }

    public static void enleverMettreEnAvant(Long id) {
        if(id != null) {
            Evenement evenement = Evenement.findById(id);
            evenement.enAvant = false;
            evenement.save();
            flash.success("Cet événement n'est plus mis en avant sur la page d'accueil du site");
            index();
        }

        index();
    }


    public static void editer(Long id) {
        Evenement evenement = Evenement.findById(id);
        flash.put("urlImage", evenement.urlImage);
        flash.put("titre", evenement.titre);
        flash.put("description", evenement.description);
        flash.put("dateDebut", evenement.dateDebut != null ? JavaExtensions.format(evenement.dateDebut, "dd/MM/yyyy HH:mm") : "");
        flash.put("dateFin",   evenement.dateFin != null ? JavaExtensions.format(evenement.dateFin, "dd/MM/yyyy HH:mm") : "");
        flash.put("lieu", evenement.lieu);
        flash.put("id", evenement.id);
        flash.put("lat", evenement.lat);
        flash.put("lon", evenement.lon);
        render();
    }

    public static void editerPost(@Required Long id, String urlImage, @Required String titre, String description, @As("dd/MM/yyyy HH:mm") @Required Date dateDebut, @As("dd/MM/yyyy HH:mm") Date dateFin, String lieu, String lat, String lon) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            editer(id);
        }

        Evenement evenement = Evenement.findById(id);
        if(evenement != null) {
            evenement.urlImage = urlImage;
            evenement.titre = titre;
            evenement.description = description;
            evenement.dateDebut = dateDebut;
            evenement.dateFin = dateFin;
            evenement.lieu = lieu;
            evenement.lat = lat;
            evenement.lon = lon;
            evenement.save();

            flash.success("L'évenement est ajouté");
        } else {
            flash.error("Une erreur est survenue.");
        }

        index();
    }
}
