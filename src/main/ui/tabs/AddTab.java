package main.ui.tabs;

import main.model.Combo;
import main.model.MatchInfo;
import main.ui.RecorderApp;

import javax.swing.*;
import java.awt.*;

// Represents a tab that is used to add matches to the records in the RecorderApp
// Code regarding textField and viewArea were influenced by TextDemo.java from Swing component demos
public class AddTab extends Tab {
    Combo combo = new Combo();                      // Combo from the input
    JTextField comboInput = makeTextField();        // Input for a combo move
    JTextField followUpInput = makeTextField();     // Input for a possible follow up
    JTextField characterInput = makeTextField();    // Input for the character that is using the combo
    JTextArea comboView = makeViewArea();           // The view area that shows the current combo
    JTextArea followUpView = makeViewArea();        // The view area that shows the follow ups loaded
    private JTextField p1Input = makeTextField();   // Input for player 1 name
    private JTextField p2Input = makeTextField();   // Input for player 2 name
    private JComboBox<String> outcomeInput;         // The input for which character won

    // EFFECTS: constructs the tab for adding a match
    public AddTab(RecorderApp controller) {
        super(controller);

        setLayout(new GridLayout(1, 1, 0, 0));
        comboView.setEditable(false);
        followUpView.setEditable(false);

        placeNames();
        placeOutcome();
        placeCombo();
        placeSubmit();
    }

    // MODIFIES: this
    // EFFECTS:  adds the inputs for the characters used
    private void placeNames() {
        JPanel playerBoxes = new JPanel();
        JLabel player1 = new JLabel("Player 1: ", JLabel.CENTER);
        JLabel player2 = new JLabel("Player 2: ", JLabel.CENTER);

        JPanel player1Row = formatRow(player1);
        player1Row.add(p1Input);
        JPanel player2Row = formatRow(player2);
        player2Row.add(p2Input);
        playerBoxes.add(player1Row);
        playerBoxes.add(player2Row);
        this.add(playerBoxes);
    }

    // MODIFIES: this
    // EFFECTS:  adds the outcome input
    // Code influenced by ComboBoxDemo.java from Swing component demos
    private void placeOutcome() {
        String[] options = {"Player 1", "Player 2"};
        JLabel label = new JLabel("Who won?", JLabel.CENTER);
        outcomeInput = new JComboBox<>(options);
        JPanel optionsRow = formatRow(label);
        optionsRow.add(outcomeInput);
        this.add(optionsRow);
    }

    // MODIFIES: this
    // EFFECTS:  adds the panel to add a combo and followUp
    private void placeCombo() {
        JPanel comboRow = new JPanel();
        comboRow.setLayout(new GridLayout(3,1));
        comboRow.add(makeCharacterInput());
        comboRow.add(makeComboInput());
        comboRow.add(makeFollowUpInput());

        this.add(comboRow);
    }

    // EFFECTS: makes the input for character input
    private JPanel makeCharacterInput() {
        JPanel characterRow = new JPanel();
        JLabel characterText = new JLabel("Character: ", JLabel.LEFT);
        characterRow.add(characterText);
        characterRow.add(characterInput);

        return characterRow;
    }

    // EFFECTS: makes the input for combo moves
    private JPanel makeComboInput() {
        JPanel comboPanel = new JPanel();
        JLabel comboText = new JLabel("Combo: \n(Type in move then press enter)", JLabel.CENTER);
        comboInput.addActionListener(e -> {
            String move = updateViewArea(comboInput, comboView);
            combo.addMove(move);
        });
        comboPanel.add(comboText);
        comboPanel.add(comboInput);
        comboPanel.add(new JScrollPane(comboView));

        return comboPanel;
    }

    // EFFECTS: makes the input for followUps
    private JPanel makeFollowUpInput() {
        JPanel followUpPanel = new JPanel();
        JLabel followUpText = new JLabel("<html>Note about Combo: <br/> "
                + "(Type in oki then press enter)</html>", JLabel.CENTER);
        followUpPanel.add(followUpText);
        followUpPanel.add(followUpInput);
        followUpInput.addActionListener(e -> {
            String followUp = updateViewArea(followUpInput, followUpView);
            combo.addFollowUps(followUp);
        });
        followUpPanel.add(new JScrollPane(followUpView));

        return followUpPanel;
    }

    // EFFECTS: makes a JTextArea
    private static JTextArea makeViewArea() {
        return new JTextArea(10, 20);
    }

    // EFFECTS: makes a JTextField
    private static JTextField makeTextField() {
        return new JTextField(20);
    }

    // EFFECTS: updates the viewArea and gets the input's and saves it
    private static String updateViewArea(JTextField input, JTextArea output) {
        String inputString = input.getText();
        output.append(inputString + "\n");
        input.selectAll();
        return inputString;
    }

    // MODIFIES: this, RecorderApp
    // EFFECTS:  places a submit button that adds the match information to the Records
    private void placeSubmit() {
        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> {
            String name = characterInput.getText();
            MatchInfo match = new MatchInfo(p1Input.getText(), p2Input.getText(), determineOutcome());
            addCombo(match, name);
            getRecords().addMatchInfo(match);
            clearAll();
        });

        this.add(submit);
    }

    // EFFECTS: takes in the index of the outcomeInput and returns a boolean;
    private boolean determineOutcome() {
        int index = outcomeInput.getSelectedIndex();
        return index == 0;
    }

    // EFFECTS: sets the combo to the matchInfo
    private void addCombo(MatchInfo match, String name) {
        if (!combo.getComboMoves().isEmpty()) {
            combo.setCharacter(name);
            match.setCombo(combo);
        }
    }

    // EFFECTS: resets all the inputs and the textArea
    private void clearAll() {
        p1Input.setText(null);
        p2Input.setText(null);
        combo = new Combo();
        comboInput.setText(null);
        comboView.setText(null);
        followUpInput.setText(null);
        followUpView.setText(null);
        characterInput.setText(null);
    }
}
