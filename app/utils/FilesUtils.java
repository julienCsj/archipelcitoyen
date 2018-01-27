package utils;

public class FilesUtils {
    public static String getExtension(String filename) {
        if (filename.contains(".")) {
            String ext = filename.substring(filename.lastIndexOf("."));
            if (ext.length() <= 6) { // si l'extension a plus de 6 caractères => ça n'est pas une extension
                return ext;
            }
        }
        return "";
    }
}
