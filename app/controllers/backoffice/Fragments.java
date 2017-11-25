package controllers.backoffice;

import models.Fragment;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.List;

public class Fragments extends SecureController {

    public static void index() {
        List<Fragment> fragments = Fragment.findAll();
        render(fragments);
    }

    public static void ajouter() {
        render();
    }

    public static void ajouterPost(@Required String nom, @Required String description, @Required String contenu) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            ajouter();
        }

        Fragment fragment = new Fragment();
        fragment.nom = nom;
        fragment.description = description;
        fragment.contenu = contenu;
        fragment.save();

        flash.success("Le fragment est ajoutée");
        index();
    }

    public static void supprimer(Long id) {
        if(id != null) {
            Fragment.delete("id = ?", id);
            flash.success("Fragment supprimé");
            index();
        }

        index();
    }

    public static void editer(Long id) {
        Fragment fragment = Fragment.findById(id);
        flash.put("nom", fragment.nom);
        flash.put("description", fragment.description);
        flash.put("contenu", fragment.contenu);
        flash.put("id", fragment.id);
        render();
    }

    public static void editerPost(@Required Long id, @Required String nom, @Required String description, @Required String contenu) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            editer(id);
        }

        Fragment fragment = Fragment.findById(id);
        if(fragment != null) {
            fragment.nom = nom;
            fragment.description = description;
            fragment.contenu = contenu;
            fragment.save();

            flash.success("Le fragment est ajouté");
        } else {
            flash.error("Une erreur est survenue.");
        }

        index();
    }
}
