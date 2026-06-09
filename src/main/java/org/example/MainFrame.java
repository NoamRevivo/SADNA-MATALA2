import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MainFrame extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private ApiClient apiClient;
    private RenderConfig currentConfig;
    private MazeModel mazeModel;
    private PathFinder pathFinder;
    private AnimationController animController;

    private SetupPanel setupPanel;
    private MazeDisplayPanel mazePanel;
    private JButton checkSolutionButton;

    public MainFrame() {
        super("מבוך ויזואלי מתוך תמונה - תרגיל Java");

        apiClient = new ApiClient();
        mazeModel = new MazeModel();
        animController = new AnimationController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null); // ממקם את החלון באמצע המסך
        setLayout(new BorderLayout());

        setupPanel = new SetupPanel();
        mazePanel = new MazeDisplayPanel();

        checkSolutionButton = new JButton("Check Solution");
        checkSolutionButton.setEnabled(false); // חסום ללחיצה עד שהמבוך יטען

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(checkSolutionButton);

        add(setupPanel, BorderLayout.NORTH);
        add(new JScrollPane(mazePanel), BorderLayout.CENTER); // מוסיפים גלילה במקרה של מבוך גדול
        add(bottomPanel, BorderLayout.SOUTH);

        applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        setupListeners();

        // בעת פתיחת התוכנית ניגשים ישר לשרת להביא את ההגדרות
        fetchConfigFromServer();
    }

    private void setupListeners() {
        setupPanel.getRefreshButton().addActionListener(event -> fetchConfigFromServer());

        setupPanel.getGetMazeButton().addActionListener(event -> fetchMazeFromServer());

        checkSolutionButton.addActionListener(event -> {

            // מניעת לחיצות נוספות בזמן שהאנימציה רצה
            if (animController.isAnimating()) {
                return;
            }

            pathFinder = new PathFinder(mazeModel);
            List<Point> solution = pathFinder.findSolution();

            if (solution.isEmpty()) {
                JOptionPane.showMessageDialog(this, "אין פתרון חוקי עבור המבוך הזה.", "תוצאת סריקה", JOptionPane.INFORMATION_MESSAGE);
            } else {
                animController.startAnimation(solution, currentConfig.getAnimationDelayMs(), mazePanel);
            }
        });
    }

    private void fetchConfigFromServer() {
        try {
            currentConfig = apiClient.fetchRenderConfig();
            setupPanel.updateConfigDisplay(currentConfig);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "שגיאה במשיכת נתונים: " + ex.getMessage(), "שגיאה", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchMazeFromServer() {
        if (currentConfig == null) {
            JOptionPane.showMessageDialog(this, "הגדרות עדיין לא נטענו.", "התראה", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int width = setupPanel.getValidatedWidth();
        int height = setupPanel.getValidatedHeight();

        try {
            BufferedImage mazeImage = apiClient.fetchMazeImage(width, height);
            mazeModel.decodeImage(mazeImage);

            mazePanel.setMazeData(mazeModel, currentConfig);
            checkSolutionButton.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "שגיאה בהורדת התמונה: " + ex.getMessage(), "שגיאה", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // משפר את הנראות של רכיבי Swing כך שייראו כמו חלונות רגילים של מערכת ההפעלה
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}