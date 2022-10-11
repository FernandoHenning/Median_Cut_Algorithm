package model;

import java.awt.Color;

public class PixelData {
    public Color color;
    public int x;
    public int y;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRed(){
        return color.getRed();
    }

    public int getGreen(){
        return color.getGreen();
    }

    public int getBlue(){
        return color.getBlue();
    }

    @Override
    public String toString() {
        return "PixelData{" +
                "color=" + color +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
