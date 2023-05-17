import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Class that draws and initializes things in World
//Takes in user input to create the game environment the user wants
//Draws every frame
public class DrawInit extends JPanel implements KeyListener {
    //int to store screen width
    public static final int WIDTH = 600;

    //int to store screen height

    public static final int HEIGHT = 600;

    //int to store half of screen width/height (for calculations)

    public static final int HALFWIDTH = WIDTH / 2;

    //int to store FPS
    public static final int FPS = 200;

    //booleans to switch modes/features on
    boolean gridOn = false;
    boolean pathOn = false;
    boolean menuOn = true;
    boolean gamemodeOn = true;
    boolean mapSelectOn = true;
    boolean loadingOn = true;
    boolean controlDisplayOn = true;

    //int to store selected map
    int mapSelected = 0;
    //create world
    World world = new World(WIDTH, HEIGHT);;

    //draw init constructor
    public DrawInit() {
        //add key listener and set panel size
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //create and start thread
        Thread mainThread = new Thread(new Runner());
        mainThread.start();
    }


    //runner class
    //Runs the drawing every frame
    class Runner implements Runnable {
        public void run() {
            while (true) {
                //while loading is not active, update objects normally
                if (!loadingOn) {
                    world.updateObjects(1.0 / (double) FPS, FPS);
                }
                repaint();
                //if loading is active
                if (loadingOn && !controlDisplayOn) {
                    try {
                        //pause to "load"
                        Thread.sleep(1700);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //sets loading to not happen the next frame
                    loadingOn = false;
                }
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException ignored) {
                }
            }

        }

    }

    //keyboard/notification methods that need to be implemented for keylistener

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();

    }

    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
    }

    //main method for keybinds
    public void keyTyped(KeyEvent e) {
        //Gets the character that was just pressed
        char c = e.getKeyChar();
        switch (c) {
            //Player 1 Movement binds
            case 'w' -> {
                //Moves up
                World.red.velocity.y = -World.redSpeed;
                World.red.velocity.x = 0;
            }
            case 'a' -> {
                //Moves left
                World.red.velocity.x = -World.redSpeed;
                World.red.velocity.y = 0;
            }
            case 's' -> {
                //Moves down
                World.red.velocity.y = World.redSpeed;
                World.red.velocity.x = 0;
            }
            case 'd' -> {
                //Moves right
                World.red.velocity.x = World.redSpeed;
                World.red.velocity.y = 0;
            }

            //Player 2 Movement binds
            //Each case checks to see if player 2 mode is active
            case 'i' -> {
                //Moves up
                if (World.player2Active) {
                    World.blue.velocity.y = -World.speed;
                    World.blue.velocity.x = 0;
                }
            }
            case 'j' -> {
                //Moves left
                if (World.player2Active) {
                    World.blue.velocity.x = -World.speed;
                    World.blue.velocity.y = 0;
                }
            }
            case 'k' -> {
                //Moves down
                if (World.player2Active) {
                    World.blue.velocity.y = World.speed;
                    World.blue.velocity.x = 0;
                }
            }
            case 'l' -> {
                //Moves right
                if (World.player2Active) {
                    World.blue.velocity.x = World.speed;
                    World.blue.velocity.y = 0;
                }
            }
            //select map, mode, grid and path status based on key pressed
            case '1' -> {
                //if mapSelectOn, set map 1 then reset map select
                if (mapSelectOn && !menuOn) {
                    mapSelected = 1;
                    World.gameMap = new Objects.Map(mapSelected);
                    world = new World(WIDTH, HEIGHT);
                    mapSelectOn = false;
                }
                //if gameModeOn, turn on player2 and get out of gameMode select
                else if (gamemodeOn) {
                    World.player2Active = true;
                    gamemodeOn = false;
                }
                //if debug key is pressed, turn on grid
                else {
                    gridOn = !gridOn;
                }
            }
            case '2' -> {
                //set map 2
                if (mapSelectOn && !menuOn) {
                    mapSelected = 2;
                    World.gameMap = new Objects.Map(mapSelected);
                    mapSelectOn = false;
                } else if (gamemodeOn && !mapSelectOn) {
                    World.player2Active = false;
                    gamemodeOn = false;
                } else {
                    pathOn = !pathOn;
                }
            }
            case '3' -> {
                //set map 3
                if (mapSelectOn && !menuOn) {
                    mapSelected = 3;
                    World.gameMap = new Objects.Map(mapSelected);
                    mapSelectOn = false;
                } else if (gamemodeOn && !mapSelectOn) {
                    World.slimesActive = true;
                    World.player2Active = false;
                    //call slime initialization
                    for (int j = 0; j < 5; j++) {
                        World.initializeSlimes(j);
                    }
                    gamemodeOn = false;
                }
            }
            case '4' -> {
                //set map 4
                if (mapSelectOn && !menuOn) {
                    mapSelected = 4;
                    World.gameMap = new Objects.Map(mapSelected);
                    mapSelectOn = false;
                }
            }
            //go past menu when space is pressed
            case ' ' -> {
                if (menuOn) {
                    menuOn = false;
                } else if (controlDisplayOn && !mapSelectOn && !gamemodeOn) {
                    controlDisplayOn = false;
                }
            }
            case '/' -> {
                if (!menuOn && !mapSelectOn) {
                    //reset all vars
                    newRun();
                    //remove and dispose of existing frame
                    Main.frame.removeAll();
                    Main.frame.dispose();
                    //create new frame
                    Main.frame = new JFrame();
                    //create new game env
                    Main.frame = new JFrame("Red vs Blue");
                    Main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    //create game env and add to frame
                    Main.gamePanel = new DrawInit();
                    Main.gamePanel.menuOn = false;
                    Main.frame.add(Main.gamePanel);
                    //set properties of frame
                    Main.frame.pack();
                    Main.frame.setContentPane(Main.gamePanel);
                    Main.frame.setVisible(true);
                    repaint();
                }
            }
            case '.' ->{
                //Exits the whole program
                System.exit(1);
            }
        }
    }

    //reset all booleans for a new run
    public void newRun() {
        loadingOn = true;
        mapSelectOn = true;
        gamemodeOn = true;
        controlDisplayOn = true;
        world.reset();
        gridOn = false;
        pathOn = false;
        mapSelected = 0;
        World.gameMap = null;
        if (World.slimesActive) {
            World.slimes.clear();
        }
        World.slimesActive = false;
        World.player2Active = false;
    }

    //drawing methods
    public void initializePlayers(Graphics g) {
        //initialize red
        g.setColor(World.red.color);
        World.red.drawPlayer(g);
        //initialize blue
        if (!World.slimesActive) {
            g.setColor(World.blue.color);
            World.blue.drawPlayer(g);
        }
        //initialize slime
        else {
            g.setColor(World.slimes.get(0).color);
            for (int i = 0; i < World.slimes.size(); i++) {
                World.slimes.get(i).drawPlayer(g);
            }
        }
    }

    public void drawTimer(Graphics g) {
        //draw white block behind both to ensure readability
        g.setColor(new Color(236, 236, 237));
        g.fillRect(240, 2, 120, 60);
        //Set color and font
        g.setColor(Color.BLACK);
        g.setFont(new Font("Georgia", Font.PLAIN, 40));
        //draw normal timer
        g.drawString(world.count + "", DrawInit.HALFWIDTH - 38, 38);
        //set color and draw high score counter
        g.setColor(new Color(255, 0, 0));
        g.drawString(world.highestCount + "", DrawInit.HALFWIDTH + 12, 38);

    }

    public void drawGrid(Graphics g) {
        //draw grid
        g.setColor(Color.BLACK);
        //loop through width and draw line
        for (int x = 0; x <= WIDTH; x++) {
            g.drawLine((int) (x * Node.size), 0, (int) (x * Node.size), (int) (WIDTH * Node.size));
        }
        //loop through height and draw line
        for (int y = 0; y <= HEIGHT; y++) {
            g.drawLine(0, (int) (y * Node.size), (int) (WIDTH * Node.size), (int) (y * Node.size));
        }
    }

    public void drawPath(Graphics g) {
        //loop through all nodes
        for (int col = 0; col < World.maxCol; col++) {
            for (int row = 0; row < World.maxRow; row++) {
                //if the node is open and on the path
                if (PathFinder.nodeArr[col][row].open && PathFinder.nodeArr[col][row].onPath) {
                    //set the path color
                    g.setColor(new Color(55, 255, 0, 61));
                    //draw a rectangle on that node
                    g.fillRect((int) (col * Node.size), (int) (row * Node.size), (int) Node.size, (int) Node.size);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        //if main menu is true
        if (menuOn) {
            //draw main menu
            super.paintComponent(g);
            UIPanel.mainMenu(g);
        }
        //if map select is true
        else if (mapSelectOn) {
            //draw map select screen
            super.paintComponent(g);
            UIPanel.mapSelect(g);
        }
        //if game mode is ready to be selected
        else if (gamemodeOn) {
            //draw mode select screen
            super.paintComponent(g);
            UIPanel.modeSelect(g);
        }
        //if game mode
        else if (controlDisplayOn) {
            //draw control display screen
            super.paintComponent(g);
            UIPanel.controlDisplay(g);
        }
        //if loading is happening
        else if (loadingOn) {
            //draw load screen
            super.paintComponent(g);
            UIPanel.loadingScreen(g);
        }
        //when not loading, call paint component
        if (!loadingOn) {
            super.paintComponent(g);
            //initialize players
            initializePlayers(g);
            //draw map
            World.gameMap.drawMap(g);
            //draw debugger
            if (gridOn) {
                drawGrid(g);
            }
            if (pathOn) {
                drawPath(g);
            }
            //draw timer
            drawTimer(g);
        }
    }
}