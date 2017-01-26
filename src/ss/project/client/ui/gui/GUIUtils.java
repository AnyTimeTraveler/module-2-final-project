package ss.project.client.ui.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A util class to create buttons and labels of the same type across all menus.
 * <p>
 * Created by fw on 22/01/2017.
 */
public class GUIUtils {
    public static JButton createButton(String text) {
        JButton result = new JButton(text);

        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        return result;
    }

    public static JLabel createLabel(String text) {
        return createLabel(text, LabelType.DEFAULT);
    }

    public static JLabel createLabel(String text, LabelType labelType) {
        JLabel result = new JLabel(text, SwingConstants.CENTER);

        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        switch (labelType) {
            case DEFAULT: {
                break;
            }
            case TITLE: {
                Font font = new Font(result.getFont().getFontName(), Font.BOLD, 27);
                result.setFont(font);
                break;
            }
        }

        return result;
    }

    public static JCheckBox createCheckBox(String text) {
        JCheckBox result = new JCheckBox(text);

        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        return result;
    }

    public static JSpinner createSpinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }

    public enum LabelType {
        DEFAULT,
        TITLE
    }
}
