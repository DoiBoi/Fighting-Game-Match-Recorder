package main.ui.tabs;

import main.model.Combo;
import main.model.MatchInfo;
import main.model.Records;
import main.ui.RecorderApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Represents a tab that lets you view the matches in records
public class ViewTab extends Tab {

    private JTable view;                            // The JTable that is used to view the matches
    private DefaultTableModel viewData;             // The data for the table
    private Records records = getRecords();         // The records
    private JPanel filterPanel = new JPanel();      // The panel for filtering matches
    private JPanel getMatchPanel = new JPanel();    // The panel for getting specific matches
    private JButton confirm;                        // The button to filter matches
    // The view area for the selected match
    private JTextArea matchViewArea = new JTextArea(25, 30);

    // EFFECTS: constructs the tab
    // Code influenced by TableDemo.java from the Swing component demos
    public ViewTab(RecorderApp controller) {
        super(controller);
        setLayout(new GridLayout(1,2));
        view = new JTable();
        update(records.getMatchInfos());
        JScrollPane scrollPane = new JScrollPane(view);
        JPanel optionsMenu = new JPanel();
        optionsMenu.setLayout(new GridLayout(2,1));
        makeFilter();
        getSpecificMatch();
        optionsMenu.add(filterPanel);
        optionsMenu.add(getMatchPanel);
        this.add(scrollPane);
        this.add(optionsMenu);
    }

    // MODIFIES: this
    // EFFECTS:  updates the records and record data (from RecorderApp)
    //           method is made public for sideTab to refresh the data
    public void updateTable() {
        records = getRecords();
        update(records.getMatchInfos());
    }

    // EFFECTS: updates the JTable
    private void update(List<MatchInfo> matches) {
        viewData = new DefaultTableModel();
        makeData(matches);
        view.setModel(viewData);
    }

    // MODIFIES: this
    // EFFECTS:  takes a list of matches (records) and adds it into the table model
    private void makeData(List<MatchInfo> matches) {
        viewData.addColumn("Player 1");
        viewData.addColumn("Player 2");
        viewData.addColumn("Winner");
        for (MatchInfo match: matches) {
            String p1 = match.getCharacter(1);
            String p2 = match.getCharacter(2);
            String winner = determineWinner(match);
            viewData.addRow(new String[]{p1, p2, winner});
        }
    }

    // EFFECTS: gets the character that won the match
    private String determineWinner(MatchInfo match) {
        if (match.getOutcome()) {
            return match.getCharacter(1);
        } else {
            return match.getCharacter(2);
        }
    }

    // MODIFIES: this
    // EFFECTS:  makes the filter panel and it's functionality
    private void makeFilter() {
        filterPanel.add(new JLabel("Type Character to filter by: ", JLabel.LEFT));
        JTextField input = new JTextField(20);
        confirm = new JButton("Filter");
        JButton reset = new JButton("Reset View");
        input.addActionListener(e -> {
            filterTable(input);
        });
        confirm.addActionListener(e -> {
            filterTable(input);
        });
        reset.addActionListener(e -> {
            updateTable();
        });
        filterPanel.add(input);
        filterPanel.add(confirm);
        filterPanel.add(reset);
    }

    // MODIFIES: this
    // EFFECTS:  updates the JTable given the filtered records
    private void filterTable(JTextField input) {
        List<MatchInfo> filteredMatches = records.filterMatches(input.getText());
        update(filteredMatches);
    }

    // MODIFIES: this
    // EFFECTS:  adds panel that gets a specific match and it's functionality
    private void getSpecificMatch() {
        getMatchPanel.add(new JLabel("Click on the row of the match where you want to get more info"));
        view.getSelectionModel().addListSelectionListener(e -> {
            int index = view.getSelectedRow();
            if (index >= 0) {
                MatchInfo match = getRecords().getMatchInfos().get(index);
                matchViewArea.setText(matchToString(match));
            }
        });
        JScrollPane matchScroll = new JScrollPane(matchViewArea);
        getMatchPanel.add(matchScroll);
    }

    // EFFECTS: takes the given match and returns it as a string
    private String matchToString(MatchInfo match) {
        String string = "Player 1: " + match.getCharacter(1) + "\nPlayer 2: " + match.getCharacter(2)
                + "\nWinner: " + determineWinner(match) + "\nNotes: \n" + printCombo(match);
        return string;
    }

    // EFFECTS: returns the combo in a formatted string
    private String printCombo(MatchInfo match) {
        String note = "  ";
        if (match.getCombo() == null) {
            return "  No note noted";
        } else {
            Combo combo = match.getCombo();
            note += "Character executed combo: " + combo.getCharacter() + "\n  ";
            note += "Combo: \n    ";
            for (String move : combo.getComboMoves()) {
                note += move + "\n    ";
            }
            note += "\n  ";
            note += "Notes Regarding Combo: \n    ";
            for (String followUp : combo.getFollowUps()) {
                note += followUp + "\n    ";
            }
        }

        return note;
    }
}
