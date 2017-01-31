package ss.project.client.ui.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A util class to create buttons and labels of the same type across all menus.
 * <p>
 * Created by fw on 22/01/2017.
 */
public class GUIUtils {
    /**
     * Create a button and set the maximum and preferred size.
     *
     * @param text
     * @return
     */
    public static JButton createButton(String text) {
        JButton result = new JButton(text);

        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        return result;
    }

    /**
     * Create a DEFAULT label.
     *
     * @param text
     * @return
     * @see GUIUtils#createLabel(String, LabelType)
     */
    public static JLabel createLabel(String text) {
        return createLabel(text, LabelType.DEFAULT);
    }

    /**
     * Create a label of specified type. The type changes the font size and bold.
     *
     * @param text      Text that needs to be shown on the label.
     * @param labelType Type of label.
     * @return A new Label instance.
     */
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

    /**
     * Create a new textfield and set it's preferred and maximum size.
     *
     * @param text Default text that needs to be shown in the textfield.
     * @return A new JTextField with default settings.
     */
    public static JTextField createTextField(String text) {
        JTextField result = new JTextField(text);
        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));
        return result;
    }

    /**
     * Create a new JCheckBox with preferred and maximum size set.
     *
     * @param text That needs ot be shown in front of the checkbox.
     * @return
     */
    public static JCheckBox createCheckBox(String text) {
        JCheckBox result = new JCheckBox(text);

        result.setPreferredSize(new Dimension(100, 50));
        result.setMaximumSize(new Dimension(150, 70));

        return result;
    }

    /**
     * Create a new SpinnerNumberSpinner with stepsize 1.
     *
     * @param value The start value.
     * @param min   Minimum value.
     * @param max   Maximum value.
     * @return
     */
    public static JSpinner createSpinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }

    /**
     * LabelType, used for presets.
     */
    public enum LabelType {
        /**
         * Default text, does nothing special.
         */
        DEFAULT,
        /**
         * Bigger font, with bold.
         */
        TITLE
    }
}
