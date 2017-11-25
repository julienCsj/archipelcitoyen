package controllers.frontoffice;

import models.Compte;
import models.Evenement;
import models.SlideAccueil;
import play.data.validation.Validation;
import play.mvc.Controller;

import javax.xml.bind.ValidationEvent;
import java.util.List;

public class PublicFront extends Controller {

    public static void index() {

        List<SlideAccueil> slides = SlideAccueil.findAll();
        Evenement evenement = Evenement.find("enAvant = true").first();
        
        render(slides, evenement);
    }

    public static void ajouterCompte() {
        render();
    }

    public static void ajouterComptePost(String nom, String prenom, String motDePasse, String email) {

        if(nom == null || nom.isEmpty()) {
            Validation.addError("nom", "Le nom est obligatoire");
        }

        if(prenom == null || prenom.isEmpty()) {
            Validation.addError("prenom", "Le prenom est obligatoire");
        }

        if(motDePasse == null || motDePasse.isEmpty()) {
            Validation.addError("motDePasse", "Le mot de passe est obligatoire");
        }

        if(email == null || email.isEmpty()) {
            Validation.addError("email", "L'email est obligatoire");
        }


        if(Validation.hasErrors()) {
            Validation.keep();
            flash.keep();
            ajouterCompte();
        }

        Compte compte = new Compte(email, nom, prenom, motDePasse);
        compte.save();

        flash.success("Votre compte est crée. Vous pourrez accéder à l'interface d'aministration une fois qu'il sera validé");
        flash.keep();
        index();

    }

}
