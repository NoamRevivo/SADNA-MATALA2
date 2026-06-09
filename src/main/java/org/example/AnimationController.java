package org.example;
import java.awt.Point;
import java.util.List;
public class AnimationController
{
    private boolean isAnimationRunning;
    public AnimationController() {
        this.isAnimationRunning = false;
    }
    public void startAnimation(List<Point> path, int delayMs, MazeDisplayPanel displayPanel) {
        if (path == null || path.isEmpty()) return;
        isAnimationRunning = true;
        displayPanel.resetDisplay();
        Thread animationThread = new Thread(() -> {
            try {
                for (Point step : path) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        displayPanel.addPathStep(step);
                    });
                    Thread.sleep(delayMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopAnimation();
        });

        animationThread.start();
    }
    private void stopAnimation() {

        isAnimationRunning = false;
    }
    public boolean isAnimating() {
        return isAnimationRunning;
    }
}