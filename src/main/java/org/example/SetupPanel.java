import javax.swing.*;
import java.awt.*;

public class SetupPanel extends JPanel {

    private static final int DEFAULT_MAZE_SIZE = 30;
    private static final int MIN_MAZE_SIZE = 5;
    private static final int MAX_MAZE_SIZE = 100;

    private static final int TEXT_FIELD_COLUMNS = 5;
    private static final int PANEL_H_GAP = 15;
    private static final int PANEL_V_GAP = 10;

    private JTextField widthField;
    private JTextField heightField;
    private JButton refreshButton;
    private JButton getMazeButton;
    private JLabel configDetailsLabel;

    public SetupPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, PANEL_H_GAP, PANEL_V_GAP));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        widthField = new JTextField(String.valueOf(DEFAULT_MAZE_SIZE), TEXT_FIELD_COLUMNS);
        heightField = new JTextField(String.valueOf(DEFAULT_MAZE_SIZE), TEXT_FIELD_COLUMNS);

        refreshButton = new JButton("רענן הגדרות");
        getMazeButton = new JButton("GET MAZE");
        configDetailsLabel = new JLabel("ממתין להגדרות מהשרת...");

        add(new JLabel("רוחב:"));
        add(widthField);
        add(new JLabel("גובה:"));
        add(heightField);
        add(refreshButton);
        add(getMazeButton);
        add(configDetailsLabel);
    }

    public void updateConfigDisplay(RenderConfig config) {
        if (config == null) return;

        String displayInfo = String.format("<html>צבע קיר: %s | צבע נתיב: %s | רשת: %b | השהיה: %d מ\"ש</html>",
                config.getWallCellColor(),
                config.getPathColor(),
                config.isDrawGrid(),
                config.getAnimationDelayMs());

        configDetailsLabel.setText(displayInfo);
    }

    // מתודה פנימית שבודקת אם המשתמש הכניס מספר תקין
    private int validateDimension(String textInput) {
        try {
            int value = Integer.parseInt(textInput.trim());
            if (value >= MIN_MAZE_SIZE && value <= MAX_MAZE_SIZE) {
                return value;
            }
        } catch (NumberFormatException e) {
            // במקרה של טקסט במקום מספר - נתעלם ונקפוץ להודעת השגיאה
        }

        JOptionPane.showMessageDialog(this, "הערך חייב להיות מספר בין 5 ל-100. הוגדר ל-30 כברירת מחדל.", "שגיאת קלט", JOptionPane.WARNING_MESSAGE);
        return DEFAULT_MAZE_SIZE;
    }

    public int getValidatedWidth() { return validateDimension(widthField.getText()); }
    public int getValidatedHeight() { return validateDimension(heightField.getText()); }

    public JButton getRefreshButton() { return refreshButton; }
    public JButton getGetMazeButton() { return getMazeButton; }
}