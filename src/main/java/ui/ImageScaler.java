package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public final class ImageScaler {

    private ImageScaler() {}

    public static ImageIcon loadAndScale(String resourcePath, int width, int height) {
        URL url = ImageScaler.class.getResource(resourcePath);
        if (url == null) {
            return null;
        }

        ImageIcon raw = new ImageIcon(url);
        return scale(raw, width, height);
    }

    public static ImageIcon scale(ImageIcon icon, int width, int height) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }

        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static ImageIcon scaleToFit(ImageIcon icon, int maxWidth, int maxHeight) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }

        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        double scale = Math.min(
                (double) maxWidth / w,
                (double) maxHeight / h
        );

        int newW = (int) (w * scale);
        int newH = (int) (h * scale);

        return scale(icon, newW, newH);
    }
}