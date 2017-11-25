package controllers.backoffice;

import models.SlideAccueil;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.List;

public class Sliders extends SecureController {

    public static void index() {
        List<SlideAccueil> slides = SlideAccueil.findAll();
        render(slides);
    }

    public static void ajouter() {
        render();
    }

    public static void ajouterPost(@Required String urlImage, String titre, String sousTitre, String urlBouton, String texteBouton) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            ajouter();
        }

        SlideAccueil slide = new SlideAccueil();
        slide.urlImage = urlImage;
        slide.titre = titre;
        slide.sousTitre = sousTitre;
        slide.urlBouton = urlBouton;
        slide.texteBouton = texteBouton;
        slide.save();

        flash.success("La slide est ajoutée");
        index();
    }

    public static void supprimerSlide(Long id) {
        if(id != null) {
            SlideAccueil.delete("id = ?", id);
            flash.success("Slide supprimée");
            index();
        }

        index();
    }

    public static void editer(Long id) {
        SlideAccueil slide = SlideAccueil.findById(id);
        flash.put("urlImage", slide.urlImage);
        flash.put("titre", slide.titre);
        flash.put("sousTitre", slide.sousTitre);
        flash.put("texteBouton", slide.texteBouton);
        flash.put("urlBouton", slide.urlBouton);
        flash.put("id", slide.id);
        render();
    }

    public static void editerPost(@Required Long idSlide, @Required String urlImage, String titre, String sousTitre, String urlBouton, String texteBouton) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            editer(idSlide);
        }

        SlideAccueil slide = SlideAccueil.findById(idSlide);
        if(slide != null) {
            slide.urlImage = urlImage;
            slide.titre = titre;
            slide.sousTitre = sousTitre;
            slide.urlBouton = urlBouton;
            slide.texteBouton = texteBouton;
            slide.save();

            flash.success("La slide est ajoutée");
        } else {
            flash.error("Une erreur est survenue.");
        }

        index();
    }
}
