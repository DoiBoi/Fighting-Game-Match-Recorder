package main.ui;

import main.model.*;
import main.persistence.JsonReader;
import main.persistence.JsonWriter;
import main.ui.tabs.AddTab;
import main.ui.tabs.PersistenceTab;
import main.ui.tabs.Splash;
import main.ui.tabs.ViewTab;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// EFFECTS: Represents a match recording application using a GUI
// Code regarding reading/writing JSON file influenced by:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Code regarding GUI implementation/refactoring influenced by SmartHome practice
public class RecorderApp extends JFrame {

    private static final String JSON_STORE = "./data/records.json";
    private Scanner input;
    private MatchInfo match;
    private Records records;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public static final int ADD_TAB_INDEX = 0;
    public static final int VIEW_TAB_INDEX = 1;
    public static final int FILE_TAB_INDEX = 2;

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private JTabbedPane sidebar;
    private EventLog log = EventLog.getInstance();

    // EFFECTS: constructs and runs the application
    public RecorderApp() throws FileNotFoundException {
        super("Fighting Game Recorder App");
        setSize(WIDTH, HEIGHT);

        makeSaveOnClose();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        records = new Records();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);

        loadTabs();
        add(sidebar);
    //  runRecorder();

        // Runs the splash screen visual
        Splash splashScreen = new Splash();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // EFFECTS: constructs the window that makes the gives the options to save before closing
    private void makeSaveOnClose() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                String[] options = {"Yes","No"};
                int promptResult = JOptionPane.showOptionDialog(null,
                        "Do you want to save before exiting?", "Save?",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        options,options[1]);
                printLog(log);
                if (promptResult == 0) {
                    saveRecords();
                    System.exit(0);
                }
            }
        });
    }

    private void printLog(EventLog el) {
        String logString = "";
        for (Event next : el) {
            logString += next.toString() + "\n";
        }

        System.out.println(logString);
    }

    // EFFECTS: Returns the records
    public Records getRecords() {
        return records;
    }

    // MODIFIES: sidebar
    // EFFECTS:  instantiates and adds the tab to the main JFrame
    private void loadTabs() {
        JPanel addTab = new AddTab(this);
        ViewTab viewTab = new ViewTab(this);
        JPanel persistenceTab = new PersistenceTab(this);
        sidebar.addChangeListener(c -> {
            viewTab.updateTable();
        });
        sidebar.add(addTab, ADD_TAB_INDEX);
        sidebar.setTitleAt(ADD_TAB_INDEX, "Add Matches");
        sidebar.add(viewTab, VIEW_TAB_INDEX);
        sidebar.setTitleAt(VIEW_TAB_INDEX, "View Matches");
        sidebar.add(persistenceTab, FILE_TAB_INDEX);
        sidebar.setTitleAt(FILE_TAB_INDEX, "Save/Load File");
    }

    // MODIFIES: this
    // EFFECTS:  processes user input
    private void runRecorder() {
        boolean active = true;
        while (active) {
            displayMenu();
            String command = input.next().toLowerCase();
            if (command.equals("q")) {
                remindToSave();
                System.out.println("Goodbye!");
                active = false;
            } else {
                processInput(command);
            }
        }
    }

    // EFFECTS: displays main menu for options
    private void displayMenu() {
        System.out.println("\nSelect from: \na -> add a match "
                + "\nv -> view matches played " + "\ne -> edit matches"
                + "\ns -> save matches to file" + "\nl -> load matches from file"
                + "\nq -> quit");
    }

    // MODIFIES: this
    // EFFECTS:  processes user command
    private void processInput(String in) {
        switch (in) {
            case "a":
                addMatch();
                break;
            case "v":
                viewMatches();
                break;
            case "e":
                editMatches();
                break;
            case "s":
                saveRecords();
                break;
            case "l":
                loadRecords();
                break;
            default:
                System.out.println("Invalid Input...");
        }
    }

    // EFFECTS: capitalizes the given string
    private String capitalize(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    // EFFECTS: produces a boolean based on the string given
    private boolean determineOutcome() {
        String outcome = checkYesOrNoInput();
        return outcome.equals("y");
    }

    // EFFECTS: check whether if the input is either a "y" or 'n"; if not print "Invalid Input"
    //          until input is "y" or "n"
    private String checkYesOrNoInput() {
        String in = input.next().toLowerCase();
        while (!(in.equals("y") || in.equals("n"))) {
            System.out.println("Invalid Input...");
            in = input.next().toLowerCase();
        }
        return in;
    }

    // EFFECTS: returns the winner based on the outcome
    private String determineWinner(MatchInfo match) {
        if (match.getOutcome()) {
            return match.getCharacter(1);
        } else {
            return match.getCharacter(2);
        }
    }

    // MODIFIES: this
    // EFFECTS:  creates a MatchInfo instance based on given information and adds combo if user wants
    private void addMatch() {
        System.out.println("Type in Character of Player 1:");
        String player1 = input.next();
        System.out.println("Type in Character of Player 2:");
        String player2 = input.next();
        System.out.println("Did player 1 win?" + "\n y -> yes \n n -> no");
        match = new MatchInfo(capitalize(player1), capitalize(player2), determineOutcome());
        records.addMatchInfo(match);
        System.out.println("Do you want to add a combo? \ny -> yes\nn -> no");
        String command = checkYesOrNoInput();
        if (command.equals("y")) {
            Combo combo = comboMaker();
            match.setCombo(combo);
        }
    }

    // EFFECTS: creates a combo and follow-ups, and returns it
    private Combo comboMaker() {
        boolean edit = true;
        System.out.println("Which character is executing the combo?");
        Combo c = new Combo(input.next());
        System.out.println("Type in a move, then press enter then type in next move\nto quit type 'quit'");
        while (edit) {
            String move = input.next();
            if (move.equals("quit")) {
                edit = false;
            } else {
                c.addMove(move);
            }
        }
        System.out.println("Are there any Okizeme options after the combo?\ny -> yes\nn -> no");
        String command = checkYesOrNoInput();
        if (command.equals("y")) {
            addFollowUp(c);
        }
        return c;
    }

    // MODIFIES: this
    // EFFECTS:  adds follow up(s) to combo
    private void addFollowUp(Combo c) {
        boolean edit = true;
        while (edit) {
            System.out.println("Type in Okizeme options:");
            c.addFollowUps(input.next());
            System.out.println("Are there any more?\ny -> yes\nn -> no");
            String command = checkYesOrNoInput();
            if (command.equals("n")) {
                edit = false;
            }
        }
    }

    // EFFECTS: prints out matches in list of recorded matches
    private void viewMatches() {
        if (records.getMatchInfos().isEmpty()) {
            System.out.println("There are no recorded matches!");
        } else {
            printListOfMatch();
            additionalOptions();
        }
    }

    // EFFECTS: prints out the list of matches
    private void printListOfMatch() {
        for (MatchInfo match: records.getMatchInfos()) {
            printOutMatch(match);
        }
    }

    // EFFECTS: creates a menu with other viewing options
    private void additionalOptions() {
        boolean loop = true;
        displayOptions();
        String command = input.next().toLowerCase();
        while (loop) {
            while (!(command.equals("s") || command.equals("w") || command.equals("f") || command.equals("q"))) {
                System.out.println("Invalid Input...");
                command = input.next().toLowerCase();
            }
            if (command.equals("q")) {
                loop = false;
            } else {
                processOptions(command);
                printListOfMatch();
                displayOptions();
                command = input.next().toLowerCase();
            }
        }
    }

    // EFFECTS: prints out the additional options for viewing
    private static void displayOptions() {
        System.out.println("Do you want to view the a specific match or view a character's win rate"
                + "\ns -> view specific match\nw -> get win rate\nf -> filter matches based on character"
                + "\nq -> return to main menu");
    }


    // EFFECTS: processes the options for additional viewings
    private void processOptions(String command) {
        if (command.equals("s")) {
            match = getSpecificMatch();
            viewCombo(match);
            viewFollowUp(match);
        } else if (command.equals("w")) {
            System.out.println("Type in name of the character:");
            command = capitalize(input.next());
            getWinRate(command);
        } else if (command.equals("f")) {
            filterMatches();
        }
    }

    // EFFECTS: Prints out combo in a form of a list of moves
    private void viewCombo(MatchInfo m) {
        if (m.getCombo() == null) {
            System.out.println("There is no combo stored in this match");
        } else {
            System.out.println("Combo noted:");
            for (String s: m.getCombo().getComboMoves()) {
                System.out.println(s);
            }
        }
    }

    // EFFECTS: Print out follow-ups
    private void viewFollowUp(MatchInfo m) {
        if (m.getCombo() == null) {
            System.out.println("No Okizeme notes");
        } else {
            System.out.println("Okizeme noted:");
            for (String followUp: m.getCombo().getFollowUps()) {
                System.out.println(followUp);
            }
        }
    }

    // EFFECTS: filters the matched based on inputted character and prints out filtered matches
    private void filterMatches() {
        System.out.println("Please type in the name of the character you want to filter by: ");
        String command = input.next();
        command = capitalize(command);
        if (records.filterMatches(command).isEmpty()) {
            System.out.println("There are no matches recorded with given name!");
        } else {
            for (MatchInfo match : records.filterMatches(command)) {
                printOutMatch(match);
            }
            getWinRate(command);
        }
    }

    // EFFECTS: prints out win-rate for a specific character
    private void getWinRate(String name) {
        if (records.filterMatches(name).isEmpty()) {
            System.out.println("There are no matches recorded with given name!");
        } else {
            System.out.println("Searching");
            System.out.println(name + "'s Win Rate is: " + records.getWinRate(name) + "%");
        }
    }

    // EFFECTS: prints out the matches listed in a numbered list and the user can input a number
    //          to fetch a specific match
    private MatchInfo getSpecificMatch() {
        for (int i = 0; i < records.getMatchInfos().size(); i++) {
            match = records.getMatchInfos().get(i);
            System.out.println((i + 1) + " -> Player 1: " + match.getCharacter(1)
                                    + " | Player 2: " + match.getCharacter(2)
                                + " | Winner: " + determineWinner(match) + " |");
        }
        System.out.println("Please input the number of the match you want to view");
        int index = Integer.parseInt(input.next());
        while (index > records.getMatchInfos().size()) {
            System.out.println("This number is out of bounds, please pick a number that was listed above");
            index = Integer.parseInt(input.next());
        }
        match = records.getMatchInfos().get(index - 1);
        printOutMatch(match);
        return match;
    }

    // EFFECTS: prints out statistic of given match
    private void printOutMatch(MatchInfo m) {
        System.out.println("Player 1: " + m.getCharacter(1)
                    + " | Player 2: " + m.getCharacter(2)
                    + " | Winner: " + determineWinner(m) + " |");
    }

    // MODIFIES: this
    // EFFECTS:  user picks the match they want to edit
    private void editMatches() {
        match = getSpecificMatch();
        editMode(match);
    }

    // MODIFIES: this
    // EFFECTS:  displays the edit options and process
    private void editMode(MatchInfo m) {
        boolean edit = true;
        while (edit) {
            System.out.println("What do you want to do with the match\nc -> change character"
                    + "\ncc -> change combo/okizeme options" + "\no -> change result\nr -> remove match\nq -> return");
            String command = input.next().toLowerCase();
            if (command.equals("c")) {
                changeCharacter(m);
            } else if (command.equals("cc")) {
                changeCombo(m);
            } else if (command.equals("o")) {
                changeOutcome(m);
            } else if (command.equals("r")) {
                edit = removeMatch(m, edit);
            } else if (command.equals("q")) {
                edit = false;
            } else {
                System.out.println("Invalid Input...");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  removes the match from records
    private boolean removeMatch(MatchInfo m, boolean edit) {
        if (!records.removeMatchInfo(m)) {
            System.out.println("Could not remove the match info (Did not exist!)");
        } else {
            System.out.println("Removed");
            edit = false;
        }
        return edit;
    }

    // MODIFIES: this
    // EFFECTS:  sets the combo stored in given match and change follow-ups if user desires
    private void changeCombo(MatchInfo m) {
        System.out.println("Current Combo Stored:");
        viewCombo(m);
        System.out.println("Do you want to change the combo?\ny -> yes\nn -> no");
        String command = checkYesOrNoInput();
        if (command.equalsIgnoreCase("y")) {
            Combo combo = comboMaker();
            m.setCombo(combo);
        } else {
            System.out.println("Do you want to change the Okizeme after the combo \ny -> yes \nn -> no");
            command = checkYesOrNoInput();
            if (command.equalsIgnoreCase("y")) {
                editFollowUp(m);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  edits follow-ups in given match
    private void editFollowUp(MatchInfo m) {
        Combo c = m.getCombo();
        viewFollowUp(m);
        System.out.println("Do you want to remove or add Okizeme options? \na -> add\nr -> remove"
                            + "\nq -> return");
        String command = input.next().toLowerCase();
        while (!(command.equals("a") || command.equals("r") || command.equals("q"))) {
            System.out.println("Invalid Input...");
            command = input.next().toLowerCase();
        }
        if (command.equals("a")) {
            addFollowUp(c);
        } else if (command.equals("r")) {
            viewFollowUp(m);
            System.out.println("Type in the Okizeme option you want to remove");
            while (!c.removeFollowUp(input.next())) {
                System.out.println("Okizeme does not exist...");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  changes character of player 1 or 2 in given match
    private void changeCharacter(MatchInfo m) {
        System.out.println("Which player do you want to edit? \n1 -> player 1\n2 -> player 2");
        int p = Integer.parseInt(input.next());
        System.out.println("What character did player " + p + " use?");
        String command = capitalize(input.next());
        m.setCharacter(p, command);
    }

    // MODIFIES: this
    // EFFECTS: changes outcome in given match
    private void changeOutcome(MatchInfo m) {
        System.out.println("Did player 1 win?");
        m.setOutcome(determineOutcome());
    }

    // EFFECTS: saves the current records onto JSON_STORE
    public void saveRecords() {
        try {
            jsonWriter.open();
            jsonWriter.write(records);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException fe) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS:  loads the records from JSON_STORE and overwrites the records
    public void loadRecords() {
        try {
            records = jsonReader.read();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException ie) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS:  reminds the user to save their records
    private void remindToSave() {
        System.out.println("Do you want to save before quitting?\ny -> yes\nn -> no");
        String input = checkYesOrNoInput();
        if (input.equalsIgnoreCase("y")) {
            saveRecords();
        }
    }
}
