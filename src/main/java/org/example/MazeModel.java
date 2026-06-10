package org.example;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class MazeModel
{
    private boolean[][] mazeData;
    private int width;
    private int height;
    public void decodeImage(BufferedImage image)
    {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.mazeData = new boolean[height][width];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixelData = image.getRGB(x, y);
                Color pixelColor = new Color(pixelData, true);
                boolean isWhite = (pixelColor.getRed() > 240 &&
                        pixelColor.getGreen() > 240 &&
                        pixelColor.getBlue() > 240);

                mazeData[y][x] = isWhite;
            }
        }
    }
    public boolean isWall(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return true;
        }
        return !mazeData[y][x];
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}