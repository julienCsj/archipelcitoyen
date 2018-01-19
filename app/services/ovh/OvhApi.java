package services.ovh;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple low level wrapper over the OVH REST API.
 *
 *
 * @author mbsk
 *
 */
public class OvhApi {

    private final String endpoint;
    private final String appKey;
    private final String appSecret;
    private final String consumerKey;

    private final static Map<String, String> endpoints;

    static {
        endpoints = new HashMap<>();
        endpoints.put("ovh-eu", "https://eu.api.ovh.com/1.0");
        endpoints.put("ovh-ca", "https://ca.api.ovh.com/1.0");
        endpoints.put("kimsufi-eu", "https://eu.api.kimsufi.com/1.0");
        endpoints.put("kimsufi-ca", "https://ca.api.kimsufi.com/1.0");
        endpoints.put("soyoustart-eu", "https://eu.api.soyoustart.com/1.0");
        endpoints.put("soyoustart-ca", "https://ca.api.soyoustart.com/1.0");
        endpoints.put("runabove", "https://api.runabove.com/1.0");
        endpoints.put("runabove-ca", "https://api.runabove.com/1.0");
    }

    public OvhApi(String endpoint, String appKey, String appSecret, String consumerKey) {
        this.endpoint = endpoint;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.consumerKey = consumerKey;
    }

    private void assertAllConfigNotNull() throws OvhApiException{
        if(endpoint==null || appKey==null || appSecret==null || consumerKey==null) {
            throw new OvhApiException("", OvhApiException.OvhApiExceptionCause.CONFIG_ERROR);
        }
    }

    public String get(String path) throws OvhApiException {
        assertAllConfigNotNull();
        return get(path, "", true);
    }

    public String get(String path, boolean needAuth) throws OvhApiException {
        assertAllConfigNotNull();
        return get(path, "", needAuth);
    }

    public String get(String path, String body, boolean needAuth) throws OvhApiException {
        assertAllConfigNotNull();
        return call("GET", body, appKey, appSecret, consumerKey, endpoint, path, needAuth);
    }

    public String put(String path, String body, boolean needAuth) throws OvhApiException {
        assertAllConfigNotNull();
        return call("PUT", body, appKey, appSecret, consumerKey, endpoint, path, needAuth);
    }

    public String post(String path, String body, boolean needAuth) throws OvhApiException {
        assertAllConfigNotNull();
        return call("POST", body, appKey, appSecret, consumerKey, endpoint, path, needAuth);
    }

    public String delete(String path, String body, boolean needAuth) throws OvhApiException {
        assertAllConfigNotNull();
        return call("DELETE", body, appKey, appSecret, consumerKey, endpoint, path, needAuth);
    }

    private String call(String method, String body, String appKey, String appSecret, String consumerKey, String endpoint, String path, boolean needAuth) throws OvhApiException
    {

        try {
            String indexedEndpoint = endpoints.get(endpoint);
            endpoint = (indexedEndpoint==null)?endpoint:indexedEndpoint;

            URL url = new URL(new StringBuilder(endpoint).append(path).toString());

            // prepare
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod(method);
            request.setReadTimeout(30000);
            request.setConnectTimeout(30000);
            request.setRequestProperty("Content-Type", "application/json");
            request.setRequestProperty("X-Ovh-Application", appKey);
            // handle authentification
            if(needAuth) {
                // get timestamp from local system
                long timestamp = System.currentTimeMillis() / 1000;

                // build signature
                String toSign = new StringBuilder(appSecret)
                        .append("+")
                        .append(consumerKey)
                        .append("+")
                        .append(method)
                        .append("+")
                        .append(url)
                        .append("+")
                        .append(body)
                        .append("+")
                        .append(timestamp)
                        .toString();
                String signature = new StringBuilder("$1$").append(HashSHA1(toSign)).toString();

                // set HTTP headers for authentication
                request.setRequestProperty("X-Ovh-Consumer", consumerKey);
                request.setRequestProperty("X-Ovh-Signature", signature);
                request.setRequestProperty("X-Ovh-Timestamp", Long.toString(timestamp));
            }

            if(body != null && !body.isEmpty())
            {
                request.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(request.getOutputStream());
                out.writeBytes(body);
                out.flush();
                out.close();
            }


            String inputLine;
            BufferedReader in;
            int responseCode = request.getResponseCode();
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
            }

            // build response
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if(responseCode == 200) {
                // return the raw JSON result
                return response.toString();
            } else if(responseCode == 400) {
                throw new OvhApiException(response.toString(), OvhApiException.OvhApiExceptionCause.BAD_PARAMETERS_ERROR);
            } else if (responseCode == 403) {
                throw new OvhApiException(response.toString(), OvhApiException.OvhApiExceptionCause.AUTH_ERROR);
            } else if (responseCode == 404) {
                throw new OvhApiException(response.toString(), OvhApiException.OvhApiExceptionCause.RESSOURCE_NOT_FOUND);
            } else if (responseCode == 409) {
                throw new OvhApiException(response.toString(), OvhApiException.OvhApiExceptionCause.RESSOURCE_CONFLICT_ERROR);
            } else {
                throw new OvhApiException(response.toString(), OvhApiException.OvhApiExceptionCause.API_ERROR);
            }

        } catch (NoSuchAlgorithmException e) {
            throw new OvhApiException(e.getMessage(), OvhApiException.OvhApiExceptionCause.INTERNAL_ERROR);
        } catch (IOException e) {
            throw new OvhApiException(e.getMessage(), OvhApiException.OvhApiExceptionCause.INTERNAL_ERROR);
        }

    }

    public static String HashSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sha1hash.length; i++) {
            sb.append(Integer.toString((sha1hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}