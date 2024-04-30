package main.ui.tabs;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

// Represents a splash screen that plays a loading gif file
public class Splash extends JFrame {

    // EFFECTS: constructs the splash screen that plays a loading gif for a 5 seconds then closes
    public Splash() {
        super("Splash screen");
        setSize(500, 500);
        setLayout(new BorderLayout());
        Icon loadingGif = new ImageIcon("data/loadingImg.gif");
        JLabel picLabel = new JLabel(loadingGif);
        this.add(picLabel);

        setVisible(true);
        setLocationRelativeTo(null);
        toFront();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setVisible(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
