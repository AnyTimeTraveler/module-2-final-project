package ss.project.client.ui.gui;

import ss.project.client.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * Created by simon on 16.01.17.
 */
public class PNLMultiPlayerLobby extends GUIPanel {
    private JList list;
    private DefaultListModel listModel;

    public PNLMultiPlayerLobby(Controller controller) {
        super(true);
    }

    @Override
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        this.setLayout(new BorderLayout());

        listModel = new DefaultListModel();


        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(new SelectionListener());
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        this.add(listScrollPane, BorderLayout.PAGE_END);
    }

    @Override
    public void onEnter() {
        listModel.removeAllElements();

        //TODO: Create a connection and retrieve all rooms.
        listModel.addElement("a room");
        listModel.addElement("Another room");
    }

    @Override
    public void onLeave() {

    }

    /**
     * Internal class used to catch events from the list.
     */
    private class SelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                System.out.println(list.getSelectedIndex());
            }
        }
    }
}
