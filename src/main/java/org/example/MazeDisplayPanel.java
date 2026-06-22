package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MazeDisplayPanel extends JPanel
{
    private MazeModel mazeModel;
    private RenderConfig config;
    private List<Point> currentPath;

    public MazeDisplayPanel()
    {
        this.currentPath = new ArrayList<>();
    }
    public void setMazeData(MazeModel model, RenderConfig config) {
        this.mazeModel = model;
        this.config = config;
        this.currentPath.clear();
        revalidate();
        repaint();
    }
    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        if (parent instanceof JViewport) {
            return parent.getSize();
        }
        return new Dimension(600, 600);
    }

    public void addPathStep(Point point)
    {
        currentPath.add(point);
        repaint();
    }
    public void resetDisplay() {
        currentPath.clear();
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mazeModel == null || config == null) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int mazeWidth = mazeModel.getWidth();
        int mazeHeight = mazeModel.getHeight();
        int padding = 20;
        int availableWidth = Math.max(1, getWidth() - padding * 2);
        int availableHeight = Math.max(1, getHeight() - padding * 2);
        double scaleX = (double) availableWidth / mazeWidth;
        double scaleY = (double) availableHeight / mazeHeight;
        double scale = Math.min(scaleX, scaleY);
        int displayW = (int) (mazeWidth * scale);
        int displayH = (int) (mazeHeight * scale);
        int offsetX = (getWidth() - displayW) / 2;
        int offsetY = (getHeight() - displayH) / 2;
        double cellW = (double) displayW / mazeWidth;
        double cellH = (double) displayH / mazeHeight;
        Color wallColor = Color.decode(config.getWallCellColor());
        for (int row = 0; row < mazeHeight; row++) {
            for (int col = 0; col < mazeWidth; col++) {
                if (mazeModel.isWall(col, row)) {
                    g2d.setColor(wallColor);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                int x = offsetX + (int) (col * cellW);
                int y = offsetY + (int) (row * cellH);
                int w = (int) Math.ceil(cellW);
                int h = (int) Math.ceil(cellH);
                g2d.fillRect(x, y, w, h);
            }
        }
        if (config.isDrawGrid() && cellW > 3 && cellH > 3) {
            Color gridColor = Color.decode(config.getGridColor());
            g2d.setColor(new Color(gridColor.getRed(), gridColor.getGreen(), gridColor.getBlue(), 120));
            g2d.setStroke(new BasicStroke(1));

            for (int row = 0; row <= mazeHeight; row++) {
                int y = offsetY + (int) (row * cellH);
                g2d.drawLine(offsetX, y, offsetX + displayW, y);
            }
            for (int col = 0; col <= mazeWidth; col++) {
                int x = offsetX + (int) (col * cellW);
                g2d.drawLine(x, offsetY, x, displayH + offsetY);
            }
        }
        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRect(offsetX, offsetY, (int)Math.ceil(cellW), (int)Math.ceil(cellH));
        g2d.setColor(new Color(231, 76, 60));
        int endX = offsetX + (int) ((mazeWidth - 1) * cellW);
        int endY = offsetY + (int) ((mazeHeight - 1) * cellH);
        g2d.fillRect(endX, endY, (int)Math.ceil(cellW), (int)Math.ceil(cellH));
        int markerSize = (int) Math.max(15, Math.min(cellW, cellH) * 1.2);
        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRect(offsetX - markerSize, offsetY, markerSize, markerSize);
        g2d.setColor(new Color(231, 76, 60));
        g2d.fillRect(offsetX + displayW, offsetY + displayH - markerSize, markerSize, markerSize);
        if (currentPath.size() > 0) {
            Color pathColor = Color.decode(config.getPathColor());
            g2d.setColor(pathColor);
            float strokeWidth = (float) Math.max(2.0, Math.min(cellW, cellH) / 3.0);
            g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < currentPath.size() - 1; i++) {
                Point p1 = currentPath.get(i);
                Point p2 = currentPath.get(i + 1);
                int x1 = offsetX + (int) (p1.x * cellW + cellW / 2);
                int y1 = offsetY + (int) (p1.y * cellH + cellH / 2);
                int x2 = offsetX + (int) (p2.x * cellW + cellW / 2);
                int y2 = offsetY + (int) (p2.y * cellH + cellH / 2);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }
}