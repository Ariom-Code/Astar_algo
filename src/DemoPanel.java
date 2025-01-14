import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DemoPanel extends JPanel  {

    final int maxCol = 30;
    final int maxRow = 20;
    final int nodeSize = 35;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    //node
    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    //others
    boolean goalReached = false;
    int step = 0;

    public DemoPanel(){

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        //place nodes
        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow){

            node[col][row] = new Node(col, row);
            this.add(node[col][row]);

            col++;
            if(col == maxCol){
                col = 0;
                row++;
            }
        }

        //set start and goal node
        setStartNode(3,6);
        setGoalNode(11, 3);

        //place solid nodes
        setSoliDNode(10, 2);
        setSoliDNode(10, 3);
        setSoliDNode(10, 4);
        setSoliDNode(10, 5);
        setSoliDNode(10, 6);
        setSoliDNode(10, 7);
        setSoliDNode(6, 2);
        setSoliDNode(7, 2);
        setSoliDNode(8, 2);
        setSoliDNode(9, 2);
        setSoliDNode(11, 7);
        setSoliDNode(12, 7);
        setSoliDNode(6, 1);
        setSoliDNode(6, 0);
        setSoliDNode(10, 8);
        setSoliDNode(10, 9);
        setSoliDNode(10, 10);

        //set cost
        setCostOnNodes();


    }

    private void setStartNode(int col, int row){
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }
    private void setGoalNode(int col, int row){
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }
    private void setSoliDNode(int col, int row){
        node[col][row].setAsSolid();
    }
    private void setCostOnNodes(){

        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow){

            getCost(node[col][row]);
            col++;
            if(col == maxCol){
                col = 0;
                row++;
            }
        }
    }
    private void getCost(Node node){

        //get the g cost (dist form the start node)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //get the h cost (dist form the goal node)
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //get the f cost(total)
        node.fCost = node.gCost + node.hCost;

        //display the cost on node
        if(node != startNode && node != goalNode){
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost+ "</html>");
        }


    }

    public void search(){

        if(goalReached == false && step < 300){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if(row -1 >= 0){
                //open the up node
                openNode(node[col][row-1]);
            }
            if(col -1 >= 0){
                //open the left node
                openNode(node[col-1][row]);
            }
            if(row +1 < maxRow){
                //open the right node
                openNode(node[col][row+1]);
            }
            if(col +1 < maxCol){
                //open the down node
                openNode(node[col+1][row]);
            }

            //find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++){
                //check if the node's F cost is better
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //if f cost is equal check the g cost
                else if (openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            //after that we get the best node which is our next step
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalReached = true;
            }
        }


        step++;
    }

    public void autoSearch(){

        while (goalReached == false && step < 300){


            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if(row -1 >= 0){
                //open the up node
                openNode(node[col][row-1]);
            }
            if(col -1 >= 0){
                //open the left node
                openNode(node[col-1][row]);
            }
            if(row +1 < maxRow){
                //open the right node
                openNode(node[col][row+1]);
            }
            if(col +1 < maxCol){
                //open the down node
                openNode(node[col+1][row]);
            }

            //find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++){
                //check if the node's F cost is better
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //if f cost is equal check the g cost
                else if (openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }

            if (openList.isEmpty()) {
                // Aucun chemin disponible
                System.out.println("Aucun chemin disponible pour atteindre l'objectif !");
                return; // Arrêter la recherche
            }

            //after that we get the best node which is our next step
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalReached = true;
                trackPath();
            }
        }

        step++;
    }

    private void openNode(Node node){
        if (node.open == false && node.checked == false && node.solid == false){

            //if node not opened add it to the open list
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackPath(){
        //backtrack and draw the best path
        Node current = goalNode;
        while (current != startNode){

            current = current.parent;

            if(current != startNode){
                current.setAsPath();
            }
        }
    }
}
