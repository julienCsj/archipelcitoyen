package controllers.frontoffice;

import models.PagesFront;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class PagesFronts extends FrontController{

    public static void index(String slug) {
        PagesFront page = PagesFront.find("slug", slug).first();

        if(page != null) {
            render(template("frontoffice/PagesFronts/pageFront.html"), page);
        }

        notFound();
    }
}
