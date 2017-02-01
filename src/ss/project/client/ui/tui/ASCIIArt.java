package ss.project.client.ui.tui;

/**
 * Created by simon on 17.01.17.
 */
class ASCIIArt {
    static String getHeadline(String headline, int screenWidth) {
        return headline;
    }

    static String getChoiceText(String question, int screenWidth) {
        return question;
    }

    static String getChoiceItem(int index, String item, int screenWidth) {
        return "[" + index + "] " + item;
    }
}
