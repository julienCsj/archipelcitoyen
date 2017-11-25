package tags;

import groovy.lang.Closure;
import models.Fragment;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;

import java.io.PrintWriter;
import java.util.Map;

public class CustomTags extends FastTags {

    public static void _fragment(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {

        String nom = (String) args.get("nom");
        if(nom != null && !nom.isEmpty()) {
            Fragment framgment = Fragment.find("nom = ?", nom).first();
            if(framgment != null) {
                out.println(JavaExtensions.raw(framgment.contenu));
            }
        }
    }
}
