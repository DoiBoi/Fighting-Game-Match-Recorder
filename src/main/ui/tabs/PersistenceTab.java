package main.ui.tabs;

import main.ui.RecorderApp;

import javax.swing.*;
import java.awt.*;

// Represents a tab that allows the user to save and load JSON file
public class PersistenceTab extends Tab {

    JLabel text = new JLabel("Save or Load file", JLabel.CENTER); // The text prompting users to load/save
    JButton saveButton = new JButton("Save");                     // Buttons that saves the current Record
                                                                       // to a file
    JButton loadButton = new JButton("Load");                     // Buttons that loads the current Record
                                                                       // to a file


    // EFFECTS: Constructs the persistence tab
    public PersistenceTab(RecorderApp controller) {
        super(controller);
        setLayout(new GridLayout(2,1));

        makeButtons(controller);
        JPanel buttonRow = formatRow(saveButton);
        buttonRow.add(loadButton);

        this.add(text);
        this.add(buttonRow);
    }

    // MODIFIES: RecorderApp
    // EFFECTS:  Makes the buttons that calls saveRecords and loadRecords
    private void makeButtons(RecorderApp controller) {
        saveButton.addActionListener(e -> {
            controller.saveRecords();
            text.setText("Saved!");
        });
        loadButton.addActionListener(e -> {
            controller.loadRecords();
            text.setText("Loaded!");
        });
    }
}
