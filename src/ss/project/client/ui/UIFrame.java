package ss.project.client.ui;

/**
 * Created by fw on 25/01/2017.
 */
public interface UIFrame {
    void switchTo(UIPanel uiPanel);

    void init();

    void dispose();

    void setSize(int width, int height);

    void setConnected(boolean enabled);

    /**
     * Show an error on the screen.
     *
     * @param message
     */
    void showError(String message);
}
