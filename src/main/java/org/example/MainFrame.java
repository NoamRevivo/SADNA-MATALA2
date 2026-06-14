package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MainFrame extends JFrame
{//קבועים לגודל החלון
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
//
    private ApiClient apiClient;
    private RenderConfig currentConfig;
    private MazeModel mazeModel;
    private PathFinder pathFinder;
    private AnimationController animController;

    private SetupPanel setupPanel;
    private MazeDisplayPanel mazePanel;
    private JButton checkSolutionButton;

    public MainFrame() {
        super(" API "+" יצירת מבוך ויזאולי מתוך תמונת"+" JAVA "+" תרגיל ");
        apiClient = new ApiClient();
        mazeModel = new MazeModel();
        animController = new AnimationController();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setupPanel = new SetupPanel();
        mazePanel = new MazeDisplayPanel();
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel explanationLabel = new JLabel("מקרא מבוך: משבצת לבנה = מעבר חופשי |" +
                " משבצת צבעונית = קיר חסום |" +
                " נתיב צבעוני = מסלול הפתרון");
        explanationLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkSolutionButton = new JButton("Check Solution");
        checkSolutionButton.setEnabled(false);
        checkSolutionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(explanationLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(checkSolutionButton);
        add(setupPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(mazePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setupListeners();
        fetchConfigFromServer();
    }
    private void setupListeners() {
        setupPanel.getRefreshButton().addActionListener(event -> fetchConfigFromServer());
        setupPanel.getGetMazeButton().addActionListener(event -> fetchMazeFromServer());
        checkSolutionButton.addActionListener(event -> {
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
            setupPanel.setInputComponentsEnabled(false);
            currentConfig = apiClient.fetchRenderConfig();
            setupPanel.updateConfigDisplay(currentConfig);
            String configDetails = String.format(
                    "ההגדרות התקבלו מהשרת בהצלחה!\n\n" +
                            "פירוט ההגדרות:\n" +
                            "• צבע קיר: %s\n" +
                            "• צבע נתיב: %s\n" +
                            "• ציור רשת: %s\n" +
                            "• זמן השהיה (אנימציה): %d מ\"ש\n\n" +
                            "לחץ על 'אישור' כדי להמשיך, או 'קבל הגדרות חדשות' כדי לרענן שוב.",
                    currentConfig.getWallCellColor(),
                    currentConfig.getPathColor(),
                    currentConfig.isDrawGrid() ? "כן" : "לא",
                    currentConfig.getAnimationDelayMs()
            );
            Object[] options = {"אישור", "קבל הגדרות חדשות"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    configDetails,
                    "נתוני שרת נטענו",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 1) {
                fetchConfigFromServer();
                return;
            }
            setupPanel.setInputComponentsEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "שגיאה במשיכת נתונים: " + ex.getMessage(), "שגיאה", JOptionPane.ERROR_MESSAGE);
            setupPanel.setInputComponentsEnabled(true); // שחרור הרכיבים למקרה שהמשתמש ירצה לנסות שוב מהפאנל
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