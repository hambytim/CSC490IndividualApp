package com.cornez.mazechase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Random;

import java.util.Stack;

//image of robot found at https://gameartpartners.com/instructional-licenses/
//image of star found at http://www.freeiconspng.com/free-images/star-icon-19134


public class MazeCanvas extends View {

    //MAZE DIMENSIONS
    public final int COLS = 10;
    public final int ROWS = 10;
    final int N_CELLS = COLS * ROWS;
    final int OFFSET = 10;
    public float cellWidth;
    public float cellHeight;
    public float boardHeight;

    //keep track of robot position and cellID
    public int robotX = 10;
    public int robotY = 10;
    private int cellID = 0;
    private Bitmap img;
    private Bitmap star;

    //cellID and coords. of star
    private int starCell;
    private int starX;
    private int starY;

    //ARRAY OF MAZE CELLS
    public MazeCell [] board;
    private Paint paint;

    public MazeCanvas (Context context){
        super(context);
        //get width and height of screen
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float width = dm.widthPixels;
        float height = dm.heightPixels;
        boardHeight = 0.60f * height;
        cellHeight = (boardHeight - 20)/ROWS;
        cellWidth = (width-20)/COLS;
        //TASK 1: DECLARE A MAZE ARRAY OF SIZE N_CELLS TO HOLD THE CELLS
        board = new MazeCell[N_CELLS];
        //TASK 2: INSTANTIATE CELL OBJECTS FOR EACH CELL IN THE MAZE
        int cellId = 0;
        for (int r = 0; r < ROWS; r++){
            for (int c = 0; c < COLS; c++){
                //STEP 1: GENERATE A MAZE CELL WITH THE X, Y AND CELL ID
                int x =(int)(c * cellWidth + OFFSET);
                int y = (int)(r * cellHeight + OFFSET);
                MazeCell cell = new MazeCell(x, y, cellId);

                //STEP 2: PLACE THE CELL IN THE MAZE
                board[cellId] = cell;
                cellId++;
            }
        }

        //TASK 3: SET THE PAINT FOR THE MAZE
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);
        Bitmap r = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
        img = Bitmap.createScaledBitmap(r, (int)(cellWidth - 20), (int)(cellHeight - 20), true);
        //TASK 4: USE A BACKTRACKER METHOD TO BREAK DOWN THE WALLS
        backtrackMaze();


        //random generator for star cell
        Random rand = new Random();
        starCell = rand.nextInt(board.length);
        starX = board[starCell].x;
        starY = board[starCell].y;
        Bitmap s = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        star = Bitmap.createScaledBitmap(s, (int)(cellWidth - 20), (int)(cellHeight - 20), true);
    }



    public void onDraw(Canvas canvas){
        //TASK 1: FILL THE CANVAS WITH WHITE PAINT
        canvas.drawRGB(255,  255,  255);

        //TASK 2: SET THE LINES OF THE MAZE TO BLACK WITH A STROKE OF 2
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);

        //TASK 3: DRAW THE LINES FOR EVERY CELL
        for (int i = 0; i < N_CELLS; i++){
            int x = board[i].x;
            int y = board[i].y;

            if (board[i].north)
                canvas.drawLine(x,  y, x+cellWidth, y, paint);
            if (board[i].south)
                canvas.drawLine(x,  y+cellHeight, x+cellWidth, y+cellHeight, paint);
            if (board[i].east)
                canvas.drawLine(x+cellWidth,  y, x+cellWidth, y+cellHeight, paint);
            if (board[i].west)
                canvas.drawLine(x,  y, x, y+cellHeight, paint);
        }
        canvas.drawBitmap(img, robotX, robotY, paint);
        canvas.drawBitmap(star, starX, starY, paint);
    }
    /*
    This method is from Ch. 5 mazeChase tutorial.

    This method goes through each cell of the maze to make sure it has been visited
    and removes one wall to make sure that every cell in the maze is reachable

     */
    public void backtrackMaze() {
        // TASK 1: CREATE THE BACKTRACKER VARIABLES AND INITIALIZE THEM
        Stack<Integer> stack = new Stack<Integer>();
        int top;
        // TASK 2: VISIT THE FIRST CELL AND PUSH IT ONTO THE STACK
        int visitedCells = 1; // COUNTS HOW MANY CELLS HAVE BEEN VISITED
        int cellID = 0; // THE FIRST CELL IN THE MAZE
        board[cellID].visited = true;
        stack.push(cellID);
        // TASK 3: BACKTRACK UNTIL ALL THE CELLS HAVE BEEN VISITED
        while (visitedCells < N_CELLS) {
            //STEP 1: WHICH WALLS CAN BE TAKEN DOWN FOR A GIVEN CELL?
            String possibleWalls = "";
            if (board[cellID].north == true && cellID >= COLS) {
                if (!board[cellID - COLS].visited) {
                    possibleWalls += "N";
                }
            }
            if (board[cellID].west == true && cellID % COLS != 0) {
                if (!board[cellID - 1].visited) {
                    possibleWalls += "W";
                }
            }
            if (board[cellID].east == true && cellID % COLS != COLS - 1) {
                if (!board[cellID + 1].visited) {
                    possibleWalls += "E";
                }
            }
            if (board[cellID].south == true && cellID < COLS * ROWS - COLS) {
                if (!board[cellID + COLS].visited) {
                    possibleWalls += "S";
                }
            }
            //STEP 2: RANDOMLY SELECT A RANDOM WALL FROM THE AVAILABLE WALLS
            if (possibleWalls.length() > 0) {
                int index = Math.round((int)(Math.random() *possibleWalls.length()));
                char randomWall = possibleWalls.charAt(index);

                switch (randomWall) {
                    case 'N':
                        board[cellID].north = false;
                        board[cellID - COLS].south = false;
                        cellID -= COLS;
                        break;
                    case 'S':
                        board[cellID].south = false;
                        board[cellID + COLS].north = false;
                        cellID += COLS;
                        break;
                    case 'E':
                        board[cellID].east = false;
                        board[cellID + 1].west = false;
                        cellID++;
                        break;
                    case 'W':
                        board[cellID].west = false;
                        board[cellID - 1].east = false;
                        cellID--;
                }
                board[cellID].visited = true;
                stack.push(cellID);
                visitedCells++;
            }
            //IF THERE ARE NO WALLS TO BUST DOWN, BACKTRACK BY GRABBING THE TOP OF THE STACK
            else {
                top = stack.pop();
                if (top == cellID){
                    cellID = stack.pop();
                    stack.push(cellID);
                }
            }
        }
    }
    //setter method for robotX
    public void setRobotX(int robotX){
        this.robotX = robotX;
    }
    //getter method for robotX
    public int getRobotX(){
        return robotX;
    }
    //setter method for robotY
    public void setRobotY(int robotY){
        this.robotY = robotY;
    }
    //getter method for robotY
    public int getRobotY(){
        return robotY;
    }
    //setter method for cellID
    public void setCellID(int cellID){
        this.cellID = cellID;
    }
    //getter method for cellID
    public int getCellID(){
        return cellID;
    }
    //getter method for starCell
    public int getStarCell(){
        return starCell;
    }
    //setter method for starCell
    public void setStarCell(int newCell){
        this.starCell = newCell;
        updateStar();
    }
    //method to update star coordinates
    public void updateStar(){
        starX = board[starCell].x;
        starY = board[starCell].y;
    }
}
