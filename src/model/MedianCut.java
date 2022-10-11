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

    public void medianCutQuantize(PixelData[] pixelsData) throws IOException {
        int redMean = getRedAvg(pixelsData);
        int greenMean = getGreenAvg(pixelsData);
        int blueMean = getBlueAvg(pixelsData);
        updateImagePixels(redMean, greenMean, blueMean, pixelsData);
        File output_file = new File(OUTPUT_PATH);
        ImageIO.write(this.img, "jpg", output_file);
    }

    private void updateImagePixels(int redMean, int greenMean, int blueMean, PixelData[] pixels) {
        IntStream.range(0, pixels.length - 1).parallel()
                .forEach(
                        i -> {
                            int rgb = ((redMean & 0x0ff) << 16) | ((greenMean & 0x0ff) << 8) | (blueMean & 0x0ff);
                            this.img.setRGB(pixels[i].x, pixels[i].y, rgb);
                        }
                );
    }

    public static void sortByChannelValues(int redRange, int greenRange, int blueRange, PixelData[] pixelsData){
        if (greenRange >= redRange && greenRange >= blueRange) { // More green colors
            orderByGreen(pixelsData);
        } else if (blueRange >= redRange) { // More blue color
            orderByBlue(pixelsData);
        } else { // More red colors
            orderByRed(pixelsData);
        }
    }
    public static int getMaxRed(PixelData[] pixelsData) {
        PixelData maxValue = null;
        for (PixelData value : pixelsData) {
            if (value.color.getRed() > (maxValue != null ? maxValue.color.getRed() : Double.NEGATIVE_INFINITY))
                maxValue = value;
        }
        if (maxValue == null)
            throw new NoSuchElementException();
        return maxValue.color.getRed();
    }

    public static int getMinRed(PixelData[] pixelsData) {
        PixelData minValue = null;
        for (PixelData value : pixelsData) {
            if (value.color.getRed() < (minValue != null ? minValue.color.getRed() : Double.POSITIVE_INFINITY))
                minValue = value;
        }
        if (minValue == null)
            throw new NoSuchElementException();
        return minValue.color.getRed();
    }

    public static int getMaxGreen(PixelData[] pixelsData) {
        PixelData maxValue = null;
        for (PixelData value : pixelsData) {
            if (value.color.getGreen() > (maxValue != null ? maxValue.color.getGreen() : Double.NEGATIVE_INFINITY))
                maxValue = value;
        }
        if (maxValue == null)
            throw new NoSuchElementException();
        return maxValue.color.getGreen();
    }

    public static int getMinGreen(PixelData[] pixelsData) {
        PixelData minValue = null;
        for (PixelData value : pixelsData) {
            if (value.color.getGreen() < (minValue != null ? minValue.color.getGreen() : Double.POSITIVE_INFINITY))
                minValue = value;
        }
        if (minValue == null)
            throw new NoSuchElementException();
        return minValue.color.getGreen();
    }

    public static int getMaxBlue(PixelData[] pixelsData) {
        PixelData maxValue = null;
        for (PixelData value : pixelsData) {
            if (value.color.getBlue() > (maxValue != null ? maxValue.color.getBlue() : Double.NEGATIVE_INFINITY))
                maxValue = value;
        }
        if (maxValue == null)
            throw new NoSuchElementException();
        return maxValue.color.getBlue();
    }

    public static int getMinBlue(PixelData[] pixelsData) {
        PixelData minValue = null;
        for (PixelData value : pixelsData) {
            if (value.color.getBlue() < (minValue != null ? minValue.color.getBlue() : Double.POSITIVE_INFINITY))
                minValue = value;
        }
        if (minValue == null)
            throw new NoSuchElementException();
        return minValue.color.getBlue();
    }

    public static void orderByRed(PixelData[] pixelsData) {
        Arrays.sort(pixelsData, Comparator.comparing(PixelData::getRed).reversed());
    }

    public static void orderByGreen(PixelData[] pixelsData) {
        Arrays.sort(pixelsData, Comparator.comparing(PixelData::getGreen).reversed());
    }

    public static void orderByBlue(PixelData[] pixelsData) {
        Arrays.sort(pixelsData, Comparator.comparing(PixelData::getBlue).reversed());
    }

    public static int getRedAvg(PixelData[] array) {
        return Arrays.stream(array)
                .collect(Collectors.averagingInt(PixelData::getRed)).intValue();
    }

    public static int getGreenAvg(PixelData[] array) {
        return Arrays.stream(array)
                .collect(Collectors.averagingInt(PixelData::getGreen)).intValue();
    }

    public static int getBlueAvg(PixelData[] array) {
        return Arrays.stream(array)
                .collect(Collectors.averagingInt(PixelData::getBlue)).intValue();
    }
}
