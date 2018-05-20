package controllers.backoffice;

import models.Adherent;
import org.apache.commons.codec.digest.DigestUtils;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class Adherents extends SecureController {

    public static void index() {
        List<Adherent> adherents = Adherent.find("").fetch();
        render(adherents);
    }

    public static void supprimerAdherent(Long id) {
        Adherent.delete("id = ?", id);
        flash.success("L'adhérent est supprimé");
        index();
    }

    public static void aPaye(Long id) {
        Adherent adherent = Adherent.find("id = ?", id).first();
        adherent.adhesionPayee = true;
        adherent.save();
        flash.success("Le paiement de l'adhésion est enregistré");
        index();
    }
}
