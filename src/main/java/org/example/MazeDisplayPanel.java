package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MazeDisplayPanel extends JPanel
{
    private static final int CELL_SIZE_PX = 20;
    private MazeModel mazeModel;
    private RenderConfig config;
    private List<Point> currentPath;
    public MazeDisplayPanel()
    {
        this.currentPath = new ArrayList<>();
    }
    public void setMazeData(MazeModel model, RenderConfig config)
    {
        this.mazeModel = model;
        this.config = config;
        this.currentPath.clear();
        int reqWidth = model.getWidth() * CELL_SIZE_PX;
        int reqHeight = model.getHeight() * CELL_SIZE_PX;
        setPreferredSize(new Dimension(reqWidth, reqHeight));
        revalidate();
        repaint();
    }
    public void addPathStep(Point point)
    {
        currentPath.add(point);
        repaint();
    }
    public void resetDisplay()
    {
        currentPath.clear();
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (mazeModel == null || config == null) return;
        Color wallColor = Color.decode(config.getWallCellColor());
        Color gridColor = Color.decode(config.getGridColor());
        Color pathColor = Color.decode(config.getPathColor());
        int mazeWidth = mazeModel.getWidth();
        int mazeHeight = mazeModel.getHeight();
        for (int row = 0; row < mazeHeight; row++)
        {
            for (int col = 0; col < mazeWidth; col++)
            {
                if (mazeModel.isWall(col, row))
                {
                    g.setColor(wallColor);
                } else
                {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(col * CELL_SIZE_PX, row * CELL_SIZE_PX, CELL_SIZE_PX, CELL_SIZE_PX);
            }
        }
        if (config.isDrawGrid())
        {
            g.setColor(gridColor);
            for (int row = 0; row <= mazeHeight; row++)
            {
                g.drawLine(0, row * CELL_SIZE_PX, mazeWidth * CELL_SIZE_PX, row * CELL_SIZE_PX);
            }
            for (int col = 0; col <= mazeWidth; col++)
            {
                g.drawLine(col * CELL_SIZE_PX, 0, col * CELL_SIZE_PX, mazeHeight * CELL_SIZE_PX);
            }
        }
        g.setColor(pathColor);
        for (Point step : currentPath) {
            g.fillRect(step.x * CELL_SIZE_PX, step.y * CELL_SIZE_PX, CELL_SIZE_PX, CELL_SIZE_PX);
        }
    }
}