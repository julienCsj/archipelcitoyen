package services.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import models.Compte;
import models.Fichier;
import play.Play;
import play.templates.JavaExtensions;
import utils.FilesUtils;
import utils.ImagesUtils;

import java.io.File;

public class S3FileUploadService {

    private static String AWS_BUCKET_NAME       = Play.configuration.getProperty("aws.bucketName");
    private static String AWS_ACCESS_KEY_ID     = Play.configuration.getProperty("aws.accesKey");
    private static String AWS_SECRET_KEY        = Play.configuration.getProperty("aws.secret");
    private static String AWS_REGION            = "eu-west-3";


    private File file;
    private Compte compte;
    private String name;
    private S3Dossier dossier;
    private String extention;

    public S3FileUploadService(Compte compte) {
        this.dossier = S3Dossier.DEFAUT;
        this.compte = compte;
    }

    public S3FileUploadService payload(File file) {
        this.file = file;
        this.extention = FilesUtils.getExtension(file.getName());
        return this;
    }

    public S3FileUploadService name(String name) {
        this.name = name;
        return this;
    }

    public S3FileUploadService dossier(S3Dossier dossier) {
        this.dossier = dossier;
        return this;
    }

    public Fichier upload() {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            String fileName = dossier + "/" + JavaExtensions.slugify(name)+this.extention;
            PutObjectResult putObjectResult = s3client.putObject(new PutObjectRequest(AWS_BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));

            Fichier fichier = new Fichier();
            fichier.name = name;
            fichier.url = "https://s3."+AWS_REGION+".amazonaws.com/archipel-citoyen/"+dossier+"/"+JavaExtensions.slugify(name)+this.extention;
            fichier.compte = compte;

            return fichier;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(Fichier fichier) {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        String fileName = fichier.url;
        fileName = fileName.replaceAll("https://s3.eu-west-3.amazonaws.com/archipel-citoyen/", "");
        s3client.deleteObject(AWS_BUCKET_NAME, fileName);
    }

    public S3FileUploadService resize(int x, int y) {
        File fileResize = ImagesUtils.resize(this.file, x, y);
        if(fileResize != null) {
            this.file = fileResize;
        }
        return this;
    }

    public enum S3Dossier {
        TEST,
        DEFAUT,
        DOCUMENTS_CERCLE,
        IMAGES,
        PJ_ARTICLE, PRESSBOOK;
    }

}
