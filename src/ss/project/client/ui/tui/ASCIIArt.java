package ss.project.client.ui.tui;

/**
 * Created by simon on 17.01.17.
 */
public class ASCIIArt {
    public static String getHeadline(String headline, int screenWidth) {
        return headline;
    }

    public static String getMenuItem(String menuItem, int screenWidth) {
        return menuItem;
    }

    public static String getChoiceText(String question, int screenWidth) {
        return question;
    }

    public static String getChoiceItem(int index, String item, int screenWidth) {
        return "[" + index + "] " + item;
    }
}
