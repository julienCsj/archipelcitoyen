package controllers.frontoffice;

import models.Cercle;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

public class FrontController extends Controller {

    @Before
    public static void injectData() {
        List<Cercle> cercles = Cercle.find("afficherFront = true").fetch();
        renderArgs.put("crcls", cercles);
    }
}
