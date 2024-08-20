package hemmouda.joojle.gui.icons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Loads the icons from the resources
 */
public class Icons {

    public static final ImageIcon RETURN_ICON;
    public static final ImageIcon HELP_ICON;

    static {
        RETURN_ICON = loadImageIcon("return.png", 18, 18);
        HELP_ICON = loadImageIcon("help.png", 18, 18);
    }

    private static ImageIcon loadImageIcon (String name, int width, int height) {
        var image = loadImage(name);
        var scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static BufferedImage loadImage (String name) {
        String path = "/hemmouda/joojle/" + name;
        InputStream imageStream = Icons.class.getResourceAsStream(path);
        BufferedImage image = null;

        try {
            image = ImageIO.read(imageStream);
        } catch (Exception e) {
            System.err.println("Couldn't load this image: " + name);
            e.printStackTrace();
        }

        return image;
    }
}
