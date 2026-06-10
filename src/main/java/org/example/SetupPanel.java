package org.example;
import javax.swing.*;
import java.awt.*;

public class SetupPanel extends JPanel
{
    //קבועים
    private static final int DEFAULT_MAZE_SIZE = 30;
    private static final int MIN_MAZE_SIZE = 5;
    private static final int MAX_MAZE_SIZE = 100;
    private static final int TEXT_FIELD_COLUMNS = 5;
    //קבועים
    private JTextField widthField;
    private JTextField heightField;
    private JButton refreshButton;
    private JButton getMazeButton;
    private JLabel configDetailsLabel;

    public SetupPanel() {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        inputPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        widthField = new JTextField(String.valueOf(DEFAULT_MAZE_SIZE), TEXT_FIELD_COLUMNS);
        heightField = new JTextField(String.valueOf(DEFAULT_MAZE_SIZE), TEXT_FIELD_COLUMNS);
        refreshButton = new JButton("רענן הגדרות");
        getMazeButton = new JButton("GET MAZE");
        inputPanel.add(new JLabel("רוחב:"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("גובה:"));
        inputPanel.add(heightField);
        inputPanel.add(refreshButton);
        inputPanel.add(getMazeButton);
        configDetailsLabel = new JLabel("ממתין להגדרות מהשרת...", SwingConstants.CENTER);
        configDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        configDetailsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(inputPanel, BorderLayout.NORTH);
        add(configDetailsLabel, BorderLayout.SOUTH);
        setInputComponentsEnabled(false);
    }
    public void setInputComponentsEnabled(boolean enabled)
    {
        widthField.setEnabled(enabled);
        heightField.setEnabled(enabled);
        getMazeButton.setEnabled(enabled);
    }
    public void updateConfigDisplay(RenderConfig config)
    {
        if (config == null) return;
        String displayInfo = String.format("הגדרות נוכחיות: צבע קיר: %s | צבע נתיב: %s | רשת: %s | השהיה: %d מ\"ש",
                config.getWallCellColor(),
                config.getPathColor(),
                config.isDrawGrid() ? "כן" : "לא",
                config.getAnimationDelayMs());
        configDetailsLabel.setText(displayInfo);
    }
    private int validateDimension(String textInput)
    {
        try
        {
            int value = Integer.parseInt(textInput.trim());
            if (value >= MIN_MAZE_SIZE && value <= MAX_MAZE_SIZE)
            {
                return value;
            }
        }
        catch (NumberFormatException e)
        {
        }
        JOptionPane.showMessageDialog(this, "הערך חייב להיות מספר בין 5 ל-100. הוגדר ל-30 כברירת מחדל.", "שגיאת קלט", JOptionPane.WARNING_MESSAGE);
        return DEFAULT_MAZE_SIZE;
    }
    public int getValidatedWidth() { return validateDimension(widthField.getText()); }
    public int getValidatedHeight() { return validateDimension(heightField.getText()); }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getGetMazeButton() { return getMazeButton; }
}