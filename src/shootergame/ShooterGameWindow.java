package shootergame;

import javax.swing.JFrame;

public class ShooterGameWindow extends JFrame {
    ShooterGameWindow() {
        this.setTitle("Shooter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new ShooterGame());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
