package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.stream.IntStream;

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

    public void execute() throws IOException {
        int[] dataBuffInt = this.img.getRGB(0, 0, this.width, this.height, null, 0, this.width);
        PixelData[] pixelsData = getPixelsData(dataBuffInt);
        splitIntoBuckets(pixelsData, this.depth);
    }

    private void splitIntoBuckets(PixelData[] pixelsData, int depth) throws IOException {
        if (pixelsData.length == 0) {
            return;
        }
        if (depth == 0) {
            medianCutQuantize(pixelsData);
            return;
        }

        int redRange = getMaxRed(pixelsData) - getMinRed(pixelsData);
        int greenRange = getMaxGreen(pixelsData) - getMinGreen(pixelsData);
        int blueRange = getMaxBlue(pixelsData) - getMinBlue(pixelsData);

        sortByChannelValues(redRange, greenRange, blueRange, pixelsData);

        int medianIndex = (pixelsData.length + 1) / 2;
        splitIntoBuckets(Arrays.copyOfRange(pixelsData, 0, medianIndex), depth - 1);
        splitIntoBuckets(Arrays.copyOfRange(pixelsData, medianIndex, pixelsData.length), depth - 1);
    }

    private PixelData[] getPixelsData(int[] bufferedIntRGB) {
        PixelData[] pixels = new PixelData[this.height * this.width];
        IntStream.range(0, this.width).parallel()
                .forEach(
                        i -> (IntStream.range(0, this.height))
                                .forEach(j -> {
                                    int currentIndex = j * this.width + i;
                                    PixelData pixelData = new PixelData();
                                    pixelData.color = new Color(bufferedIntRGB[currentIndex]);
                                    pixelData.x = i;
                                    pixelData.y = j;
                                    pixels[currentIndex] = pixelData;
                                })
                );
        return pixels;
    }
}
