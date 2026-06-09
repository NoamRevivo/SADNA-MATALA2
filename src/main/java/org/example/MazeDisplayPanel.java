package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MazeDisplayPanel extends JPanel {

    // קבוע לגודל המשבצת כפי שהותר בהוראות המטלה
    private static final int CELL_SIZE_PX = 20;

    private MazeModel mazeModel;
    private RenderConfig config;
    private List<Point> currentPath;

    public MazeDisplayPanel() {
        this.currentPath = new ArrayList<>();
    }

    public void setMazeData(MazeModel model, RenderConfig config) {
        this.mazeModel = model;
        this.config = config;
        this.currentPath.clear();

        // מחשבים את הגודל הכולל של הפאנל כדי שיהיה אפשר לגלול אם המבוך ענק
        int panelWidth = model.getWidth() * CELL_SIZE_PX;
        int panelHeight = model.getHeight() * CELL_SIZE_PX;
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        revalidate();
        repaint();
    }

    public void addPathStep(Point point) {
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

        // אם עוד אין נתונים, אין מה לצייר
        if (mazeModel == null || config == null) return;

        Color wallColor = Color.decode(config.getWallCellColor());
        Color gridColor = Color.decode(config.getGridColor());
        Color pathColor = Color.decode(config.getPathColor());

        int mazeWidth = mazeModel.getWidth();
        int mazeHeight = mazeModel.getHeight();

        // ציור הקירות והמעברים
        for (int row = 0; row < mazeHeight; row++) {
            for (int col = 0; col < mazeWidth; col++) {

                if (mazeModel.isWall(col, row)) {
                    g.setColor(wallColor);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(col * CELL_SIZE_PX, row * CELL_SIZE_PX, CELL_SIZE_PX, CELL_SIZE_PX);
            }
        }

        // ציור הרשת במידה והשרת ביקש
        if (config.isDrawGrid()) {
            g.setColor(gridColor);

            // ציור קווים אופקיים
            for (int row = 0; row <= mazeHeight; row++) {
                g.drawLine(0, row * CELL_SIZE_PX, mazeWidth * CELL_SIZE_PX, row * CELL_SIZE_PX);
            }
            // ציור קווים אנכיים
            for (int col = 0; col <= mazeWidth; col++) {
                g.drawLine(col * CELL_SIZE_PX, 0, col * CELL_SIZE_PX, mazeHeight * CELL_SIZE_PX);
            }
        }

        // ציור נתיב הפתרון (המשבצות שהאנימציה כבר הוסיפה)
        g.setColor(pathColor);
        for (Point step : currentPath) {
            g.fillRect(step.x * CELL_SIZE_PX, step.y * CELL_SIZE_PX, CELL_SIZE_PX, CELL_SIZE_PX);
        }
    }
}