package services;

import play.Play;
import play.libs.WS;
import sun.security.provider.MD5;
import utils.StringUtils;

public class MailchimpConnector {

    private String BASEURL;
    private String APIKEY;
    private String USERNAME;
    private String PASSWORD;

    public MailchimpConnector() {
        this.BASEURL =  Play.configuration.getProperty("mailchimp.baseurl");
        this.APIKEY =   Play.configuration.getProperty("mailchimp.apikey");
        this.USERNAME = Play.configuration.getProperty("mailchimp.username");
        this.PASSWORD = Play.configuration.getProperty("mailchimp.password");
    }

    public boolean addMember(MailchimpList list, String email, String nom, String prenom) {

        if(email == null || email.isEmpty()) return false;
        if(nom == null || nom.isEmpty()) return false;
        if(prenom == null || prenom.isEmpty()) return false;

        nom = nom.toUpperCase();
        email = email.toLowerCase();

        String data = "" +
                "{" +
                "   \"email_address\":\""+email+"\", " +
                "   \"status\":\"subscribed\"," +
                "   \"merge_fields\": {" +
                "       \"FNAME\": \""+prenom+"\"," +
                "       \"LNAME\": \""+nom+"\"" +
                "   }" +
                "}";

        WS.HttpResponse rep = WS.url(BASEURL + "/lists/" + list.listID + "/members")
                .authenticate(USERNAME, PASSWORD)
                .setHeader("content-type", "application/json")
                .body(data)
                .post();

        System.out.println(rep.getString());

        return rep.getStatus() == 200;
    }

    public boolean isMemberOfList(MailchimpList list, String email) {

        if(email != null && !email.isEmpty()) {
            email = email.toLowerCase();
            String emailHash = StringUtils.toMD5(email);
            if(emailHash != null) {
                WS.HttpResponse rep = WS.url(BASEURL + "/lists/" + list.listID + "/members/"+emailHash)
                        .authenticate(USERNAME, PASSWORD)
                        .get();


                if(rep.getStatus() == 404) {
                    return false;
                } else {
                    String repAsStr = rep.getString();
                    return repAsStr.contains("subscribed");
                }
            }
        }

        return false;
    }
}
