import javax.swing.*;

//AI Tag (Red vs Blue)

//By George Henry, Ryan Hughes and Anthony Leon

//Sources
//http://www.animator.net/forum/pivotw-beginners-hall/82875-progress.html
//https://www.youtube.com/watch?v=Hd0D68guFKg&t=21s&ab_channel=RyiSnow
//https://www.w3schools.com/java/
//https://docs.oracle.com/javase/8/docs/technotes/guides/2d/spec/j2d-intro.html

public class Main {
    //create game panel and new frame
    static DrawInit gamePanel;
    static JFrame frame;

    public static void main(String[] args) {
        //create frame and set exit on close
        frame = new JFrame("Red vs Blue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //create game env and add to frame
        gamePanel = new DrawInit();
        frame.add(gamePanel);
        //set properties of frame
        frame.pack();
        frame.setContentPane(gamePanel);
        frame.setVisible(true);
    }
}
