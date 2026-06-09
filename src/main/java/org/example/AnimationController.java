import javax.swing.Timer;
import java.awt.Point;
import java.util.List;

public class AnimationController {

    private Timer timer;
    private boolean isAnimationRunning;

    public AnimationController() {
        this.isAnimationRunning = false;
    }

    public void startAnimation(List<Point> path, int delayMs, MazeDisplayPanel displayPanel) {
        if (path == null || path.isEmpty()) return;

        isAnimationRunning = true;
        displayPanel.resetDisplay();

        // משתמשים במערך בעל תא יחיד כדי שנוכל לקדם את האינדקס בתוך הפעולה של הטיימר
        final int[] currentStepIndex = {0};

        timer = new Timer(delayMs, event -> {

            if (currentStepIndex[0] < path.size()) {
                displayPanel.addPathStep(path.get(currentStepIndex[0]));
                currentStepIndex[0]++;
            } else {
                stopAnimation();
            }
        });

        timer.start();
    }

    private void stopAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        isAnimationRunning = false;
    }

    public boolean isAnimating() {
        return isAnimationRunning;
    }
}