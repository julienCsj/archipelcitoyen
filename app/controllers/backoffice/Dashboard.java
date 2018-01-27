package controllers.backoffice;

import models.Compte;
import org.apache.commons.codec.digest.DigestUtils;
import services.aws.S3FileUploadService;

import java.io.File;
import java.util.Date;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class Dashboard extends SecureController {

    public static void index() {
        Compte compte = getCompte();
        render(compte);
    }
}
