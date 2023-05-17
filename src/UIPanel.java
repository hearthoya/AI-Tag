import java.awt.*;

//Class that draws the user interface (every screen before the game starts)

public class UIPanel {
    //Create fonts
    private static final Font menuFont = new Font("Georgia", Font.BOLD, 40);
    private static final Font bigFont = new Font("Georgia", Font.BOLD, 32);
    private static final Font mediumFont = new Font("Georgia", Font.BOLD, 26);
    private static final Font smallFont = new Font("Georgia", Font.BOLD, 20);
    private static final Font smallestFont = new Font("Georgia", Font.BOLD, 13);

    //draw main menu
    public static void mainMenu(Graphics g) {
        //draw background image for main menu
        Image img = Toolkit.getDefaultToolkit().getImage("bgImage.png");
        g.drawImage(img, 0, 0, null);
        //draw names
        g.setFont(smallestFont);
        g.drawString("By George, Ryan and Anthony", 5, smallestFont.getSize());
        //set menu font and draw title
        g.setColor(Color.black);
        g.setFont(menuFont);
        g.drawString("Red vs Blue", DrawInit.WIDTH / 2 - 120, (DrawInit.HEIGHT / 2) - 120);
        //set small font and start instructions
        g.setFont(smallFont);
        g.drawString("Press Space to start", (DrawInit.WIDTH / 2) - 88, (DrawInit.HEIGHT / 2) - 80);
    }

    //draw or "display" controls
    public static void controlDisplay(Graphics g) {
        //if not in slime mode
        if (!World.slimesActive) {
            //1v1 (2 player mode)
            if (World.player2Active) {
                Image img = Toolkit.getDefaultToolkit().getImage("1v1.png");
                g.drawImage(img, 0, 0, null);
            }
            //AI mode (AI as player 2)
            if (!World.player2Active) {
                Image img = Toolkit.getDefaultToolkit().getImage("vsAI.png");
                g.drawImage(img, 0, 0, null);
            }
        }
        //if in slime mode
        if (World.slimesActive) {
            Image img = Toolkit.getDefaultToolkit().getImage("vsSlimes.png");
            g.drawImage(img, 0, 0, null);
        }

    }

    //draw loading screen for "professionalism"
    public static void loadingScreen(Graphics g) {
        //draw black background
        g.setColor(Color.black);
        g.fillRect(0, 0, World.width, World.height);
        //set text color and write loading message
        g.setFont(bigFont);
        g.setColor(Color.white);
        g.drawString("Loading...", (DrawInit.WIDTH / 2) - 71, (DrawInit.HEIGHT / 2) + 10);
    }

    //draw white panel to "wipe" screen
    public static void wipe(Graphics g) {
        //set color to white
        g.setColor(Color.white);
        //draw white frame over current frame to "wipe"
        g.fillRect(0, 0, DrawInit.WIDTH, DrawInit.HEIGHT);
    }

    //draw map selection components
    public static void mapSelect(Graphics g) {
        //wipe past screen
        wipe(g);
        //set menu font then write
        g.setColor(Color.black);
        g.setFont(menuFont);
        g.drawString("Pick a map", 200, DrawInit.HEIGHT / 2 - 100);
        //draw keybinds to exit or go back to map select
        g.setFont(smallFont);
        g.drawString("Press . to exit game at any point", 150, DrawInit.HEIGHT/ 2 +200);
        g.drawString("Press / to return to map select at any point", 95, DrawInit.HEIGHT/ 2 +225);
        //draw selection boxes for each map
        g.drawRect(65, DrawInit.HEIGHT / 2, 100, 100);
        g.drawRect(192, DrawInit.HEIGHT / 2, 100, 100);
        g.drawRect(317, DrawInit.HEIGHT / 2, 100, 100);
        g.drawRect(442, DrawInit.HEIGHT / 2, 100, 100);
        //draw numbers of maps
        g.setFont(mediumFont);
        g.drawString("Map 1", 75, 290);
        g.drawString("Map 2", 200, 290);
        g.drawString("Map 3", 325, 290);
        g.drawString("Map 4", 450, 290);
        //draw map titles with small font
        g.setFont(smallFont);
        g.drawString("Standard", 67, 357);
        g.drawString("Square", 205, 357);
        g.drawString("Random", 323, 357);
        g.drawString("Blank", 459, 357);
        //draw buttons that need to be pressed to select map
        g.setFont(smallestFont);
        g.drawString("Press 1 to select", 62, 415);
        g.drawString("Press 2 to select", 188, 415);
        g.drawString("Press 3 to select", 313, 415);
        g.drawString("Press 4 to select", 438, 415);
    }

    //draw mode selection components
    public static void modeSelect(Graphics g) {
        //wipe past screen
        wipe(g);
        //set font then write pick a game mode text
        g.setColor(Color.black);
        g.setFont(menuFont);
        g.drawString("Pick a game mode!", 110, DrawInit.HEIGHT / 2 - 100);
        //draw keybinds to enable debugging
        g.setFont(smallFont);
        g.drawString("During the game, press 1 to see the grid", 100, DrawInit.HEIGHT/ 2 +200);
        g.drawString("To see the AI pathing, press 2", 150, DrawInit.HEIGHT/ 2 +225);
        //draw mode selection boxes
        g.drawRect(100, DrawInit.HEIGHT / 2, 100, 100);
        g.drawRect(250, DrawInit.HEIGHT / 2, 100, 100);
        g.drawRect(400, DrawInit.HEIGHT / 2, 100, 100);
        //set font and draw titles for selection boxes
        g.setFont(mediumFont);
        g.setColor(Color.black);
        g.drawString("PvP", 125, DrawInit.HEIGHT / 2 - 10);
        g.drawString("AI", 285, DrawInit.HEIGHT / 2 - 10);
        g.drawString("Slimes", 406, DrawInit.HEIGHT / 2 - 10);
        //set font and draw buttons that need to be selected under each box
        g.setFont(smallestFont);
        g.drawString("Press 1 to select", 97, DrawInit.HEIGHT / 2 + 115);
        g.drawString("Press 2 to select", 247, DrawInit.HEIGHT / 2 + 115);
        g.drawString("Press 3 to select", 397, DrawInit.HEIGHT / 2 + 115);
        //draw a mini version of both players in the first box
        g.setColor(new Color(37, 37, 197));
        g.fillRect(110, 310, 20, 20);
        g.setColor(new Color(165, 37, 37));
        g.fillRect(170, 372, 20, 20);
        //draw a mini player and the AI path in second box
        g.setColor(new Color(37, 37, 197));
        g.fillRect(260, 310, 20, 20);
        g.setColor(new Color(165, 37, 37));
        g.fillRect(320, 372, 20, 20);
        g.setColor(Color.black);
        g.drawLine(284, 334, 316, 368);
        g.drawLine(316, 368, 314, 354);
        g.drawLine(316, 368, 302, 366);
        //draw the slimes and player in the third box
        g.setColor(Color.magenta);
        g.fillRect(410, 308, 20, 20);
        g.fillRect(436, 310, 20, 20);
        g.fillRect(440, 334, 20, 20);
        g.fillRect(430, 320, 20, 20);
        g.fillRect(409, 340, 20, 20);
        g.setColor(new Color(165, 37, 37));
        g.fillRect(470, 372, 20, 20);
    }
}
