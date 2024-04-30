package main.ui.tabs;

import main.model.Records;
import main.ui.RecorderApp;

import javax.swing.*;
import java.awt.*;

// Represents a tab
public abstract class Tab extends JPanel {

    private final RecorderApp recorderApp;       // A recorderApp so that a tab can modify/view the Records in it.

    // EFFECTS: constructs the tab
    public Tab(RecorderApp recorderApp) {
        this.recorderApp = recorderApp;
    }

    //EFFECTS: creates and returns row included
    public JPanel formatRow(Component b) {
        JPanel p = new JPanel();
        p.add(b);

        return p;
    }

    // EFFECTS: returns the records from the recorderApp;
    public Records getRecords() {
        return recorderApp.getRecords();
    }

}

