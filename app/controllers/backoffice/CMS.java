package controllers.backoffice;

import models.PagesFront;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.List;

public class CMS extends SecureController {

    public static void index() {
        List<PagesFront> pages = PagesFront.findAll();
        render(pages);
    }

    public static void ajouter() {
        render();
    }

    public static void ajouterPost(@Required String slug, @Required String titre, @Required String html) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            ajouter();
        }

        PagesFront pageFront = new PagesFront();
        pageFront.slug = slug;
        pageFront.titre = titre;
        pageFront.contenu = html;
        pageFront.save();

        flash.success("La page est ajoutée");
        index();
    }

    public static void supprimerPage(Long id) {
        if(id != null) {
            PagesFront.delete("id = ?", id);
            flash.success("Page supprimée");
            index();
        }

        index();
    }

    public static void editer(Long id) {
        PagesFront page = PagesFront.findById(id);
        flash.put("slug", page.slug);
        flash.put("contenu", page.contenu);
        flash.put("titre", page.titre);
        flash.put("id", page.id);
        render();
    }

    public static void editerPost(@Required Long idPage, @Required String slug, @Required String titre, @Required String html) {

        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            flash.keep();
            editer(idPage);
        }

        PagesFront pageFront = PagesFront.findById(idPage);
        if(pageFront != null) {
            pageFront.slug = slug;
            pageFront.titre = titre;
            pageFront.contenu = html;
            pageFront.save();

            flash.success("La page est ajoutée");
        } else {
            flash.error("Une erreur est survenue.");
        }

        index();
    }
}
