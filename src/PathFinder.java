import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

//Class that scans nodes and allows AI to path find
public class PathFinder {
    World world;
    //array of all nodes
    static Node[][] nodeArr;
    //list of open nodes
    ArrayList<Node> openList = new ArrayList<>();
    //list of nodes in the path
    ArrayList<Node> pathList = new ArrayList<>();
    //specifies start, goal, and current node of the pathfinding object
    Node startNode, goalNode, currentNode;
    //is on path (gets set to false when goal is reached)
    static boolean onPath = true;
    //is goal reached
    boolean goalReached = false;

    public PathFinder(World world) {
        //applies pathfinder to world
        this.world = world;
        //instantiate nodes for the pathfinder
        instantiateNodes();
    }

    //create array of nodes which are the world for the AI
    public void instantiateNodes() {
        //make a new node array
        nodeArr = new Node[World.maxCol][World.maxRow];
        int col = 0;
        int row = 0;
        //loop through the whole array and assign nodes to the array
        while (col < World.maxCol && row < World.maxRow) {
            nodeArr[col][row] = new Node(col, row);
            col++;
            if (col == World.maxCol) {
                col = 0;
                row++;
            }
        }

    }

    //reset nodes
    public void resetNodes() {
        int col = 0;
        int row = 0;
        //set all booleans assigned to all nodes as false, resetting it
        while (col < World.maxCol && row < World.maxRow) {
            nodeArr[col][row].open = false;
            nodeArr[col][row].checked = false;
            nodeArr[col][row].solid = false;
            nodeArr[col][row].onPath = false;
            col++;
            if (col == World.maxCol) {
                col = 0;
                row++;
            }
        }
        //reset lists
        openList.clear();
        pathList.clear();
        goalReached = false;
    }

    //set start state of nodes
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        //reset nodes
        resetNodes();
        //set start node
        startNode = nodeArr[startCol][startRow];
        startNode.start = true;
        //set current node
        currentNode = startNode;
        //set goal node
        goalNode = nodeArr[goalCol][goalRow];
        goalNode.goal = true;
        //add current node to open list once player has left that node
        openList.add(currentNode);
        //create loop to go through all positions
        int col = 0;
        int row = 0;
        while (col < World.maxCol && row < World.maxRow) {
            //set solid nodes
            Rectangle2D.Double solidNode = world.node[col][row].hBox;
            for (Rectangle2D.Double i : World.gameMap.map) {
                if (solidNode.intersects(i)) {
                    nodeArr[col][row].solid = true;
                }
            }
            //get cost of each node
            getCost(nodeArr[col][row]);
            //loop through  row until you reach the end then move to the next one
            col++;
            if (col == World.maxCol) {
                col = 0;
                row++;
            }
        }
    }

    //get costs for node
    public void getCost(Node node) {
        //calculate gcost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        // calculate hcost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        //calculate fcost
        node.fCost = node.gCost + node.hCost;
    }

    //searches all nodes for path from passed in player
    public boolean searchNodes(Objects.Player searching) {
        while (!goalReached) {
            //setting current position being searched to current node and row
            int col = currentNode.col;
            int row = currentNode.row;
            //mark node as checked and remove from open list
            currentNode.checked = true;
            openList.remove(currentNode);
            //open the upward node
            if (row - 1 >= 0) {
                openNode(nodeArr[col][row - 1]);
            }
            //open the left node
            if (col - 1 >= 0) {
                openNode(nodeArr[col - 1][row]);
            }
            //open the down node
            if (row + 1 < World.maxRow) {
                openNode(nodeArr[col][row + 1]);
            }
            //open the right node
            if (col + 1 < World.maxCol) {
                openNode(nodeArr[col + 1][row]);
            }
            //find the best node based on index and cost
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            //loop through the list of open nodes
            for (int i = 0; i < openList.size(); i++) {
                //reset best node based on the nodes that have been checked
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //if F cost is equal for two points check the g cost and choose that one instead
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            //if there are no open nodes close loop
            if (openList.size() == 0) {
                break;
            }
            //after the loop, open list at best node index is next step
            currentNode = openList.get(bestNodeIndex);
            //if goal is reached return true
            if (currentNode == goalNode & !world.scoreConditions(World.red, searching)) {
                trackPath();
                goalReached = true;
            }
        }
        return goalReached;
    }

    //creates open nodes
    public void openNode(Node node) {
        //add all open nodes to a list
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackPath() {
        //track path by adding nodes to a list
        Node current = goalNode;
        while (current != startNode) {
            current.onPath = true;
            pathList.add(0, current);
            current = current.parent;
        }
    }
}


class Node {
    //parent node
    Node parent;
    //size of node stored as double
    static double size = DrawInit.WIDTH * .05;
    //column position stored as int
    int col;
    //row position stored as int
    int row;
    //ints for the costs of each node
    int gCost;
    int hCost;
    int fCost;
    //boolean to check if node is the start
    boolean start;
    //boolean to check if node is the goal
    boolean goal;
    //boolean to check if node is solid (walls)
    boolean solid;
    //boolean to check if node is open
    boolean open;
    //boolean to check if node has been checked
    boolean checked;
    //boolean to check that AI is on path
    boolean onPath;

    //hit box
    Rectangle2D.Double hBox;

    //Constructor
    public Node(int col, int row) {
        this.col = col;
        this.row = row;
        //create hitbox based on pos and size
        hBox = new Rectangle2D.Double(col * size, row * size, size, size);
    }
}