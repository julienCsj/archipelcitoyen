package services.ovh;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RedirectionMail {

    public String id;
    public String from;
    public String to;

    public RedirectionMail(String redirection) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(redirection).getAsJsonObject();

        this.id = json.has("id") ? json.get("id").getAsString() : null;
        this.from = json.has("from") ? json.get("from").getAsString() : null;
        this.to = json.has("to") ? json.get("to").getAsString() : null;
    }

    public boolean isValide() {
        return id != null && from != null && to != null;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }
}
