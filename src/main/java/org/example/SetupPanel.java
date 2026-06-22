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
    private int refreshCount = 0;
    private int getMazeCount = 0;
    private JLabel countersLabel;

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
        countersLabel = new JLabel("רענונים: 0 | מבוכים: 0");
        inputPanel.add(countersLabel);
        refreshButton.addActionListener(e -> { refreshCount++; countersLabel.setText("רענונים: " + refreshCount + " | מבוכים: " + getMazeCount); });
        getMazeButton.addActionListener(e -> { getMazeCount++; countersLabel.setText("רענונים: " + refreshCount + " | מבוכים: " + getMazeCount); });
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
    private int validateField(JTextField field, String fieldName)
    {
        try
        {
            int value = Integer.parseInt(field.getText().trim());
            if (value >= MIN_MAZE_SIZE && value <= MAX_MAZE_SIZE)
            {
                field.setBackground(Color.WHITE);
                return value;
            }
        }
        catch (NumberFormatException e)
        {
        }
        field.setBackground(new Color(255, 180, 180));
        JOptionPane.showMessageDialog(this,
                "הערך בשדה " + fieldName + " חייב להיות מספר בין " + MIN_MAZE_SIZE + " ל-" + MAX_MAZE_SIZE + ".\nהוגדר ל-" + DEFAULT_MAZE_SIZE + " כברירת מחדל.",
                "שגיאת קלט",
                JOptionPane.ERROR_MESSAGE);

        field.setText(String.valueOf(DEFAULT_MAZE_SIZE));
        return DEFAULT_MAZE_SIZE;
    }

    public int getValidatedWidth() {
        return validateField(widthField, "רוחב");
    }

    public int getValidatedHeight() {
        return validateField(heightField, "גובה");
    }

    public JButton getRefreshButton() { return refreshButton; }

    public JButton getGetMazeButton() { return getMazeButton; }
}