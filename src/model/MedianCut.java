package model;

import java.awt.image.BufferedImage;

public class MedianCut {
    private final BufferedImage img;
    private final int width;
    private final int height;
    private final int depth;
    private static final String OUTPUT_PATH = "src/images/output.jpg";

    public MedianCut(BufferedImage img, int depth) {
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.depth = depth;
    }
}
