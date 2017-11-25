package controllers.backoffice;

import org.apache.commons.codec.digest.DigestUtils;
import play.*;
import play.mvc.*;

import java.security.MessageDigest;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void connexion() {

        List<Compte> comptes = Compte.findAll();
        System.out.println("comptes = " + comptes.size());
        for (Compte compte : comptes) {
            System.out.printf("compte = "+compte);
        }

        render();
    }


    public static void connexionPost(String email, String password) {
        session.clear();
        Compte compte = Compte.find("email = ?", email).first();
        String hash = DigestUtils.sha512Hex(password);

        if(compte != null) {
            if(hash.equals(compte.motDePasse)) {
                session.put("compte", compte.id);
                Dashboard.index();
            }
        }

        flash.error("Mauvais identifiants");
        connexion();
    }

    public static void deconnexion() {
        session.clear();
        flash.success("Vous êtes déconnecté");
        connexion();
    }


}