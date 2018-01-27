package utils;

import play.Play;
import play.libs.Images;
import sun.management.FileSystemImpl;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagesUtils {

    public static File resizeAndCrop(File originalImage, int maxWidth, int maxHeight) {
        File out = null;
        if (originalImage != null) {
            try {
                out = File.createTempFile("temp-", originalImage.getName().substring(originalImage.getName().lastIndexOf(".")));
                Images.resize(originalImage, out, -1, maxHeight);
                BufferedImage source = ImageIO.read(out);
                int width = source.getWidth();
                int height = source.getHeight();
                int y1 = 0;
                int y2 = height;
                int x1 = (width - maxWidth) / 2;
                int x2 = x1 + maxWidth;
                Images.crop(out, out, x1, y1, x2, y2);

            } catch (Exception e) {
                e.printStackTrace();
                out = null;
            }
        }
        return out;
    }

    public static File resize(File originalImage, int maxWidth, int maxHeight) {
        if (originalImage != null) {
            File out = null;
            try {
                out = File.createTempFile("temp-", FilesUtils.getExtension(originalImage.getPath()), Play.tmpDir);
                BufferedImage source = ImageIO.read(originalImage);
                int sourceWidth = source.getWidth();
                int sourceHeight = source.getHeight();
                int toWidth = sourceWidth;
                int toHeight = sourceHeight;
                double ratio = (double) sourceHeight / sourceWidth;

                // Si la largeur max est spécifiée ET que l'image est plus large que la largeur max
                // on redéfinie la taille par rapport à cette largeur max
                if (maxWidth > 0 && sourceWidth > maxWidth) {
                    toWidth = maxWidth;
                    toHeight = (int) (toWidth * ratio);
                }
                // Si la hauteur max est spécifiée ET que l'image (déjà retaillé par rapport à la largeur) est plus haute que la hauteur max
                // On redéfinie la taille par rapport à la hauteur
                if (maxHeight > 0 && toHeight > maxHeight) {
                    toHeight = maxHeight;
                    toWidth = (int) (toHeight / ratio);
                }


                    String mimeType = "image/jpeg";
                    if (out.getName().endsWith(".png")) {
                        mimeType = "image/png";
                    }
                    if (out.getName().endsWith(".gif")) {
                        mimeType = "image/gif";
                    }

                // out
                BufferedImage dest = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
                Image srcSized = source.getScaledInstance(toWidth, toHeight, Image.SCALE_SMOOTH);
                Graphics graphics = dest.getGraphics();
//                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, toWidth, toHeight);
                graphics.drawImage(srcSized, 0, 0, null);
                ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType).next();
                ImageWriteParam params = writer.getDefaultWriteParam();
                // Bonne Compression JPEG -> Qualité supérieure
                if (mimeType.equals("image/jpeg")) {
                    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    params.setCompressionQuality(0.9f);
                }


                FileImageOutputStream toFs = new FileImageOutputStream(out);
                writer.setOutput(toFs);
                IIOImage image = new IIOImage(dest, null, null);
                writer.write(null, image, params);
                toFs.flush();
                toFs.close();

                return out;

            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return null;
    }


}
