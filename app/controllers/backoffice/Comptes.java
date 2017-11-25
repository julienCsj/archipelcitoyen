package controllers.backoffice;

import models.Compte;
import org.apache.commons.codec.digest.DigestUtils;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class Comptes extends SecureController {

    public static void index() {
        List<Compte> comptes = Compte.find("").fetch();
        render(comptes);
    }

    public static void ajouterComptePost(@Required(message = "Le pseudo est obligatoire") String pseudo) {

        if(Validation.hasErrors()) {
            Validation.keep();
            index();
        }
    }

    public static void supprimerCompte(Long id) {
        Compte.delete("id = ?", id);
        flash.success("Le compte est supprimé");
        index();
    }

    public static void promouvoir(Long id) {
        Compte compte = Compte.find("id = ?", id).first();
        compte.groupe = Compte.Groupe.ADMINISTRATEUR;
        compte.save();
        flash.success("Le compte est ADMINISTRATEUR");
        index();
    }
}
