package org.example;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MazeModel
{
    private static final int COLOR_MAX_VALUE = 255;
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
                boolean isWhite = (pixelColor.getRed() == COLOR_MAX_VALUE &&
                        pixelColor.getGreen() == COLOR_MAX_VALUE &&
                        pixelColor.getBlue() == COLOR_MAX_VALUE);
                mazeData[y][x] = isWhite;
            }
        }
    }
    public boolean isWall(int x, int y)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
        {
            return true;
        }
        return !mazeData[y][x];
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}