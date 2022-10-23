import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame () {
        //Adds the game panel to the Frame
        GamePanel panel = new GamePanel();
        this.add(panel);

        //Sets Game Title
        this.setTitle("Snake");

        //Tells the game to close on clicking exit
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Tells the game that it is not resizable
        this.setResizable(false);

        //Adds JFrame to all added components
        this.pack();

        //Game is visible
        this.setVisible(true);

        //Null sets the location in the middle of the screen
        this.setLocationRelativeTo(null);
    }
}
