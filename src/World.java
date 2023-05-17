import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

//Class that deals with the interactions between objects
public class World {
    //world height
    static int height;
    //world width
    static int width;
    //actual timer
    int count;
    //keeps track of milliseconds
    int countCheck;
    //highest time/score
    int highestCount = 0;
    //bounds of cols and rows
    static int maxCol = (int) (DrawInit.HEIGHT / Node.size);
    static int maxRow = (int) (DrawInit.HEIGHT / Node.size);
    //array of nodes
    Node[][] node = new Node[maxCol][maxRow];
    //check if player 2 is controlled by human or ai
    static boolean player2Active;
    //check if gamemode is slimes
    static boolean slimesActive = false;

    //speed variable for enemy
    public static int speed = 250;
    //speed variable for player
    public static int redSpeed = 250;
    //create players
    final static Objects.Player red = new Objects.Player(1, 480, 480);
    final static Objects.Player blue = new Objects.Player(0, 90, 90);
    //Map Instance
    static Objects.Map gameMap;
    //linked list of slimes
    final static LinkedList<Objects.Player> slimes = new LinkedList<>();
    //create PathFinder
    final PathFinder pFinder = new PathFinder(this);

    //create game world
    public World(int initWidth, int initHeight) {
        height = initHeight;
        width = initWidth;
        count = 0;
        countCheck = 0;
        int col = 0;
        int row = 0;
        //applying nodes to the world
        while (col < maxCol && row < maxRow) {
            node[col][row] = new Node(col, row);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
    }


    //initialize Slime locations
    public static void initializeSlimes(int j) {
        //position
        int slimeX = (int) (Math.random() * width);
        int slimeY = (int) (Math.random() * width);
        //set new slime as "toAdd"
        Objects.Player toAdd = new Objects.Player(2, slimeX - (slimeX % 30), slimeY - (slimeY % 30));
        //checks if slime should be spawned at random position if certain conditions are met, if not spawns at 0,0
        for (Rectangle2D.Double i : gameMap.map) {
            if (toAdd.getBounds().intersects(i)) {
                initializeSlimes(j);
                return;
            }
        }
        if (Math.abs(toAdd.position.x - red.position.x) <= Node.size * 3 && Math.abs(toAdd.position.y - red.position.y) <= Node.size * 3) {
            initializeSlimes(j);
            return;
        }
        //adds the slime to the linked list
        slimes.add(toAdd);
    }

    //Creates A* AI
    public void AI() {
        //checks if slimes are active and if the enemy is on a path
        //if no slimes
        if (PathFinder.onPath && !slimesActive) {
            //column and row of starting node
            int startCol = (int) (blue.position.x / Node.size);
            int startRow = (int) (blue.position.y / Node.size);
            //column and row of goal node (node of player)
            int goalCol = (int) ((red.position.x / Node.size) % (Node.size));
            int goalRow = (int) ((red.position.y / Node.size) % (Node.size));
            //run search path
            blue.searchPath(startCol, startRow, goalCol, goalRow, pFinder);
            //if yes slimes
        } else if (PathFinder.onPath) {
            //gets start node, goal node, and path for each slime
            for (Objects.Player slime : slimes) {
                int startCol = (int) (slime.position.x / Node.size);
                int startRow = (int) (slime.position.y / Node.size);
                int goalCol = (int) ((red.position.x / Node.size) % (Node.size));
                int goalRow = (int) ((red.position.y / Node.size) % (Node.size));
                slime.searchPath(startCol, startRow, goalCol, goalRow, pFinder);
            }
        }
    }


    //make object/wall collisions
    public static void objectCollisions(Objects.Player player, Objects.Player player2) {
        //wall collisions
        for (Rectangle2D.Double i : gameMap.map) {
            if (player.getBounds().intersects(i)) {
                player.setPosition(player.prev);
            }
            if (player2.getBounds().intersects(i)) {
                player2.setPosition(player2.prev);
            }
        }
    }

    //AI node collisions
    public static boolean aiCollisions(Objects.Player player) {
        //loop through walls
        for (Rectangle2D.Double i : gameMap.map) {
            //if the player intersects them return true else return false
            if (player.getBounds().intersects(i)) {
                return true;
            }
        }
        return false;
    }


    //creates scoring conditions

    public boolean scoreConditions(Objects.Player red, Objects.Player blue) {
        //tagger win
        if (red.getBounds().intersects(blue.getBounds()) || blue.getBounds().intersects(red.getBounds())) {
            //increment score everytime red tags blue
            return true;
        }
        return false;
    }

    //updates movable objects
    public void updateObjects(double time, int FPS) {
        //AI
        if (!player2Active) {
            AI();
        }
        //update, positions, velocities, etc
        red.update(time);
        if (!slimesActive) {
            blue.update(time);
            //check if objects have collided
            objectCollisions(red, blue);
        } else {
            //slimes
            for (int i = 0; i < slimes.size(); i++) {
                slimes.get(i).update(time);
                //check if objects have collided
                objectCollisions(red, slimes.get(i));
            }
        }
        if (!slimesActive) {
            if (scoreConditions(red, blue)) {
                reset();
            }
        } else {
            for (int i = 0; i < slimes.size(); i++) {
                if (scoreConditions(red, slimes.get(i))) {
                    reset();
                }
            }
        }
        //timer
        //Once countCheck reaches the FPS value to indicate a second passing, we reset and increment count by 1
        if (countCheck == FPS) {
            countCheck = 0;
            //increment in seconds
            count++;
        }
        countCheck++;
    }

    //resets objects
    public void reset() {

        //reset positions and velocities
        //red reset
        red.setPosition(new Objects.Pair(480, 480));
        red.setVelocity(0, 0);
        if (!slimesActive) {
            //blue reset
            blue.setPosition(new Objects.Pair(90, 90));
            blue.setVelocity(0, 0);
        } else {
            //slimes reset
            slimes.clear();
            for (int i = 0; i < 5; i++) {
                initializeSlimes(i);
            }
        }
        //New AI
        AI();
        //reset timer
        if (count > highestCount) {
            highestCount = count;
        }
        count = 0;
        countCheck = 0;
        //pause for 2 seconds
        PathFinder.onPath = true;
        try {
            //pause in between rounds
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}




