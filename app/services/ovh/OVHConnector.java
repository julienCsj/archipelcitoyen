package services.ovh;

import com.google.gson.*;
import play.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OVHConnector {

    private static String endpoint = "ovh-eu";
    private static String appKey      = Play.configuration.getProperty("ovh.appKey");
    private static String appSecret   = Play.configuration.getProperty("ovh.appSecret");
    private static String consumerKey = Play.configuration.getProperty("ovh.consumerKey");


    public static List<RedirectionMail> getRedirections() {

        OvhApi api = new OvhApi(endpoint, appKey, appSecret, consumerKey);
        List<RedirectionMail> redirections = new ArrayList<>();

        try {
            String res = api.get("/email/domain/larchipelcitoyen.org/redirection");

            JsonParser parser = new JsonParser();
            JsonArray json = parser.parse(res).getAsJsonArray();
            List<String> ids = new ArrayList<>();

            for (JsonElement jsonElement : json) {
                ids.add(jsonElement.getAsString());
            }

            for (String id : ids) {
                String redirection = api.get("/email/domain/larchipelcitoyen.org/redirection/"+id);
                RedirectionMail redirectionMail = new RedirectionMail(redirection);
                if(redirectionMail != null && redirectionMail.isValide()) {
                    redirections.add(redirectionMail);
                }
            }
        } catch (OvhApiException e) {
            return null;
        }

        return redirections;
    }

    public static List<RedirectionMail> getRedirections(String mailingList) {
        List<RedirectionMail> redirections = getRedirections();
        List<RedirectionMail> redirectionsMailingList = new ArrayList<>();

        for (RedirectionMail redirection : redirections) {
            if(redirection.from.equals(mailingList)) {
                redirectionsMailingList.add(redirection);
            }
        }

        return redirectionsMailingList;
    }

    public static boolean createNewRedirection(String from, String to) {
        try {
            OvhApi api = new OvhApi(endpoint, appKey, appSecret, consumerKey);
            Map<String, Object> body = new HashMap<>();
            body.put("from", from);
            body.put("to", to);
            body.put("localCopy", false);

            String jsonBody = new Gson().toJson(body);
            System.out.println(jsonBody);
            api.post("/email/domain/larchipelcitoyen.org/redirection", jsonBody, true);
            return true;
        } catch (OvhApiException e) {
            return false;
        }
    }

    public static boolean deleteEmailRedirection(String id) {
        try {
            OvhApi api = new OvhApi(endpoint, appKey, appSecret, consumerKey);
            String res = api.delete("/email/domain/larchipelcitoyen.org/redirection/"+id, "", true);
            System.out.println(res);
            return true;
        } catch (OvhApiException e) {
            e.printStackTrace();
            return false;
        }
    }


}
