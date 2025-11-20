package pizzaria.view;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

public class ImageUtil {

    public static ImageIcon loadImage(String imageName) {
        try {
            URL imageUrl = ImageUtil.class.getResource("/img/" + imageName);
            if (imageUrl != null) {
                return new ImageIcon(imageUrl);
            } else {
                System.err.println("Erro: Imagem não encontrada - /img/" + imageName);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon resizeImage(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return null;
        }
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}