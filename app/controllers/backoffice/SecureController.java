package controllers.backoffice;

import models.Cercle;
import models.Compte;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class SecureController extends Controller {

    @Before
    public void injectData() {
        List<Cercle> cercles = Cercle.findAll();
        renderArgs.put("cercles", cercles);
    }

    @Before
    public void checkConnexion() {
        try {
            Long compteId = Long.parseLong(session.get("compte"));
            if(compteId == null) {
                forbidden("Il faut être connecté");
            } else {
                Compte compte = Compte.findById(compteId);
                if(compte == null) {
                    Application.connexion();
                } else {
                    renderArgs.put("compteConnecte", compte);
                }
            }
        } catch(Exception e) {
            Application.connexion();
        }

    }

    public static Compte getCompte() {
        Long compteId = new Long(session.get("compte"));
        return Compte.findById(compteId);
    }
}
