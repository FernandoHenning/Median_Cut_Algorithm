import model.MedianCut;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Main{
    private static final String INPUT_PATH = "src/images/input.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File(INPUT_PATH));
        MedianCut medianCut = new MedianCut(img, 3);
        medianCut.execute();
    }
}
