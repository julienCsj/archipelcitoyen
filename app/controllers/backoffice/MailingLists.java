package controllers.backoffice;

import services.ovh.OVHConnector;
import services.ovh.OvhApi;
import services.ovh.OvhApiException;
import services.ovh.RedirectionMail;
import utils.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class MailingLists extends SecureController {

    public static void index() {
        List<RedirectionMail> redirections = OVHConnector.getRedirections();
        if(redirections == null) {
            flash.error("Erreur lors de la récupération des données chez OVH");
            render();
        }

        Set<String> mailingLists = new HashSet<>();
        redirections.stream().filter(p -> mailingLists.add(p.getFrom())).collect(Collectors.toList());

        render(redirections, mailingLists);
    }

    public static void ajouterEmailSurMailingList(String from, String tos) {

        int nbSucces = 0;
        int nbError = 0;

        List<String> emails = StringUtils.extractEmailsFromString(tos);
        for (String to : emails) {
            boolean res = OVHConnector.createNewRedirection(from, to);
            if(res) nbSucces++; else nbError++;
        }

        if(nbSucces > 0) flash.success(nbSucces + " emails ajoutés à la mailing list "+from);
        if(nbError > 0) flash.error(nbError + " emails n'ont pas été ajoutés à la mailing list "+from);
        index();
    }

    public static void supprimerRedirection(String id) {
        boolean res = OVHConnector.deleteEmailRedirection(id);
        if(res) flash.success("Mail supprimé de la mailing list");
        else flash.error("Une erreur est survenue");

        index();
    }

    public static void supprimerToutesRedirections(String from) {
        List<RedirectionMail> redirections = OVHConnector.getRedirections();

        for (RedirectionMail redirection : redirections) {
            if(redirection.from.equals(from)) {
                OVHConnector.deleteEmailRedirection(redirection.id);
            }
        }

        index();
    }
}