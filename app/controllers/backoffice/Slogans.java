package controllers.backoffice;

import models.Slogan;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.List;

public class Slogans extends SecureController {

    public static void index() {
        List<Slogan> slogans = Slogan.findAll();
        render(slogans);
    }


    public static void ajouterPost(@Required String texte) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            index();
        }

        Slogan slogan = new Slogan();
        slogan.texte = texte;
        slogan.save();

        flash.success("Le slogan est ajouté");
        index();
    }

    public static void supprimer(Long id) {
        if(id != null) {
            Slogan.delete("id = ?", id);
            flash.success("Slogan supprimé");
            index();
        }

        index();
    }
}
