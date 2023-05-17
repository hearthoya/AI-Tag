import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;

//Class that holds all objects
public class Objects {
    //Class that holds x and y that is used for position and velocity for coordinate x and y
    static class Pair {
        //x coordinate
        public double x;
        //y coordinate
        public double y;

        //construct pair
        public Pair(double initX, double initY) {
            x = initX;
            y = initY;
        }

        //Method add that takes in Pair toAdd and adds toAdd.x to x and toAdd.y to y
        public Pair add(Pair toAdd) {
            return new Pair(x + toAdd.x, y + toAdd.y);
        }

        //Method times that takes in val and multiples x and y by val
        public Pair times(double val) {
            return new Pair(x * val, y * val);
        }
    }

    //Parent class for Player class
    static class Object {
        //position x and y
        Pair position;
        //velocity x and y
        Pair velocity;
        //acceleration x and y
        Pair acceleration;
        //color
        Color color;

        //method to set position to a new Pair of x and y vals

        public void setPosition(Pair other) {
            position = other;
        }

        //method to set velocity to a new Pair of x and y vals

        public void setVelocity(int x, int y) {
            velocity = new Pair(x, y);
        }

    }

    //Child of Object
    static class Player extends Object {
        //previous position
        Pair prev;
        //a player width relative to the width of the display defined in DrawInit
        static double width = DrawInit.WIDTH * .05;

        //player constructor
        public Player(int color, double x, double y) {
            //switch statement to determine color of player
            if (color == 1) {
                //red color
                this.color = new Color(165, 37, 37);
            } else if (color == 2) {
                this.color = Color.magenta;
            } else {
                //blue color
                this.color = new Color(37, 37, 197);
            }
            //instance vars for each player
            this.position = new Pair(x, y);
            this.velocity = new Pair(0, 0);
            this.acceleration = new Pair(0, 0);
        }

        //method which searches for paths based on the start position and goal position of player
        public void searchPath(int startCol, int startRow, int goalCol, int goalRow, PathFinder pFinder) {
            //set all nodes before searching
            pFinder.setNodes(startCol, startRow, goalCol, goalRow);
            if (pFinder.searchNodes(this)) {
                //Next position
                int nextX = (int) (pFinder.pathList.get(0).col * Node.size);
                int nextY = (int) (pFinder.pathList.get(0).row * Node.size);
                //Player sides
                int left = (int) position.x + 1;
                int right = (int) (position.x + width - 1);
                int top = (int) position.y + 1;
                int down = (int) (position.y + width - 1);
                //up
                if (top > nextY && left >= nextX && right < nextX + Node.size) {
                    setVelocity(0, -World.speed);
                }
                //down
                else if (top < nextY && left >= nextX && right < nextX + Node.size) {
                    setVelocity(0, World.speed);
                } else if (top >= nextY && down < nextY + Node.size) {
                    //left
                    if (left > nextX) {
                        setVelocity(-World.speed, 0);
                    }
                    //right
                    if (left < nextX) {
                        setVelocity(World.speed, 0);
                    }
                } else if (top - Node.size > nextY && left - Node.size > nextX) {
                    //up or left
                    setVelocity(0, -World.speed);
                    if (World.aiCollisions(this)) {
                        setVelocity(-World.speed, 0);
                    }
                } else if (top > nextY && left < nextX) {
                    //up or right
                    setVelocity(0, -World.speed);
                    if (World.aiCollisions(this)) {
                        setVelocity(World.speed, 0);
                    }
                } else if (top + Node.size < nextY && left > nextX) {
                    //down or left
                    setVelocity(0, World.speed);
                    if (World.aiCollisions(this)) {
                        setVelocity(-World.speed, 0);
                    }
                } else if (top < nextY && left < nextX) {
                    //down or right
                    setVelocity(0, World.speed);
                    if (World.aiCollisions(this)) {
                        setVelocity(-World.speed, 0);
                    }
                }
            }
        }

        public void drawPlayer(Graphics gg) {
            //cast graphics object to be 2D
            Graphics2D g = (Graphics2D) gg;
            //create player body
            Rectangle2D.Double p = new Rectangle2D.Double(position.x, position.y, width, width);
            //draw player
            g.draw(p);
            //fill player
            g.fill(p);
        }

        //get wall bounds
        public Rectangle2D.Double getBounds() {
            return new Rectangle2D.Double(position.x, position.y, width, width);
        }

        //set world bounds
        public void worldBounds() {
            //wall bounds
            if (position.x <= 0) {
                velocity.x = 0;
                position.x = 0;
            } else if (position.x + width >= DrawInit.WIDTH) {
                velocity.x = 0;
                position.x = DrawInit.HEIGHT - width;
            }
            if (position.y <= 0) {
                velocity.y = 0;
                position.y = 0;
            } else if (position.y + width >= DrawInit.HEIGHT) {
                velocity.y = 0;
                position.y = DrawInit.HEIGHT - width;
            }
        }

        //update individual objects
        public void update(double time) {
            prev = position;
            position = position.add(velocity.times(time));
            velocity = velocity.add(acceleration.times(time));
            worldBounds();
        }

    }

    static class Map {
        //make array list of walls
        static Vector<Rectangle2D.Double> map;

        //create and draw walls
        public Map(int mapSelect) {
            map = new Vector<>();
            switch (mapSelect) {
                //Default map
                case 1:
                    //middle rectangles
                    map.add(new Rectangle2D.Double(360, DrawInit.HALFWIDTH, 210, 30));
                    map.add(new Rectangle2D.Double(30, DrawInit.HALFWIDTH, 210, 30));
                    //player side rectangles
                    map.add(new Rectangle2D.Double(240, 450, 120, 30));
                    map.add(new Rectangle2D.Double(240, 150, 120, 30));
                    break;
                //Square mid map
                case 2:
                    //left side rectangle
                    map.add(new Rectangle2D.Double(90, 210, 30, 210));
                    //right side rectangle
                    map.add(new Rectangle2D.Double(510, 210, 30, 210));
                    //top side rectangle
                    map.add(new Rectangle2D.Double(210, 90, 210, 30));
                    //bottom side rectangle
                    map.add(new Rectangle2D.Double(210, 510, 210, 30));
                    //big middle square
                    map.add(new Rectangle2D.Double(210, 210, 210, 210));
                    break;
                //random map
                case 3:
                    for (int i = 0; i < 20; i++) {
                        Rectangle2D.Double toAdd = new Rectangle2D.Double((int) (Math.random() * World.maxCol) * Node.size, (int) (Math.random() * World.maxRow) * Node.size, Node.size + (int) (Math.random() * 4) * Node.size, Node.size + (int) (Math.random() * 4) * Node.size);
                        if (!(World.red.getBounds().intersects(toAdd) || (World.blue.getBounds().intersects(toAdd)))) {
                            map.add(toAdd);
                        }
                    }
                    break;
                //blank map
                case 4:
                    break;
            }
        }

        //draw method for walls
        public static void drawMap(Graphics gg) {
            //cast graphics object to be 2D
            Graphics2D g = (Graphics2D) gg;
            //set color to black
            g.setColor(Color.black);
            //go through list and draw and fill all walls
            for (Rectangle2D.Double i : map) {
                //draw walls
                g.draw(i);
                //fill walls
                g.fill(i);
            }
        }
    }
}