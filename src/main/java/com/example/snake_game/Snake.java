package com.example.snake_game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import java.util.LinkedList;
import java.util.Objects;

enum direction{
    UP,
    LEFT,
    DOWN,
    RIGHT
}
public class Snake{
    //Snake configurations/attributes
    private Color color;
    private LinkedList<Pair<Integer, Integer>> coordinates;
    public Pair<Integer, Integer> foodPos;
    private int boundX;
    private int boundY;
    private int stepSize;
    private direction dir;
    private direction oldDir;
    private boolean alive;
    private Canvas canvas;
    private GraphicsContext gc;

    public Snake(int length, int gridSize, Canvas canvas){
        this.canvas = canvas;
        this.dir = direction.RIGHT;
        this.oldDir = this.dir;
        this.color = Color.BLACK;
        this.boundX = (int)canvas.getWidth();
        this.boundY = (int)canvas.getHeight();
        this.foodPos = new Pair<>(this.boundX,this.boundY);
        this.stepSize = gridSize;
        int startX = this.boundX/2/this.stepSize*this.stepSize;
        int startY = this.boundY/2/this.stepSize*this.stepSize;
        this.alive = true;
        this.coordinates = new LinkedList<>();
        this.coordinates.add(new Pair<>(startX, startY));
        for(int i = 0; i < length-1; i++){
            grow();
        }
        gc = this.canvas.getGraphicsContext2D();
        gc.setFill(this.color);
        drawGrid();
        drawSnake();
        spawnFood();
    }
    public Snake(){}
    //adds length to the snake by adding to its tail
    public void grow(){
        //For initialization
        Pair<Integer, Integer> xy = this.coordinates.getLast();
        Pair<Integer, Integer> newPos = new Pair<>(xy.getKey()-stepSize, xy.getValue());
        this.coordinates.addLast(newPos);
    }
    //moves the snake by a fixed step size(one grid)
    public void move(){
        Pair<Integer, Integer> newFirst = nextStep();
        if(newFirst == this.coordinates.getFirst()){
            return;
        }
        this.coordinates.addFirst(newFirst);
        drawSegment(coordinates.getFirst(), this.color);
        if(!isBody(this.foodPos)){
            int lastX = this.coordinates.getLast().getKey();
            int lastY = this.coordinates.getLast().getValue();
            if((lastX+lastY)/this.stepSize%2 ==0){
                drawSegment(this.coordinates.getLast(), Color.GREEN);
            }else{
                drawSegment(this.coordinates.getLast(), Color.LIGHTGREEN);
            }
            this.coordinates.removeLast();
        }else{
            spawnFood();
        }
    }
    //returns the coordinates of the next step
    public Pair<Integer, Integer> nextStep(){
        int xPos = this.coordinates.getFirst().getKey();
        int yPos = this.coordinates.getFirst().getValue();
        //Ensures atomicity, prevent moving in opposite direction
        if(this.oldDir.ordinal() % 2 == this.dir.ordinal() % 2){
            this.dir = this.oldDir;
        }else{
            this.oldDir = this.dir;
        }
        switch (this.dir) {
            case UP -> yPos -= this.stepSize;
            case DOWN -> yPos += this.stepSize;
            case LEFT -> xPos -= this.stepSize;
            case RIGHT -> xPos += this.stepSize;
        }
        Pair<Integer, Integer> newPos = new Pair<>(xPos, yPos);
        if(xPos < 0 || xPos >= this.boundX || yPos < 0 || yPos >= this.boundY || isBody(newPos)){
            this.alive = false;
            return this.coordinates.getFirst();
        }
        return newPos;
    }
    //For keyboard input
    public void changeDir(direction direct){
        this.dir = direct;
    }
    public void drawSnake(){
        for(Pair<Integer, Integer> pair: this.coordinates){
            drawSegment(pair, this.color);
        }
    }
    public void drawSegment(Pair<Integer, Integer> seg, Color color){
        gc.setFill(color);
        gc.fillRect(seg.getKey(), seg.getValue(), this.stepSize, this.stepSize);
    }
    private void spawnFood(){
        //Generate food that's outside of snake body
        do{
            int x = (int) (Math.random()*this.boundX / this.stepSize)* this.stepSize;
            int y = (int) (Math.random()*this.boundY / this.stepSize)* this.stepSize;
            this.foodPos = new Pair<>(x,y);
        }while(isBody(this.foodPos));
        gc.setFill(Color.BLUE);
        gc.fillOval(this.foodPos.getKey(), this.foodPos.getValue(), this.stepSize, this.stepSize);
    }
    public boolean isAlive(){
        return this.alive;
    }
    public int getLength(){
        return this.coordinates.size();
    }
    //Check if the pair is in the body linked list
    private boolean isBody(Pair<Integer, Integer> pos) {
        for (Pair<Integer, Integer> pair : this.coordinates) {
            if (Objects.equals(pair.getKey(), pos.getKey()) && Objects.equals(pair.getValue(), pos.getValue())) {
                return true;
            }
        }
        return false;
    }
    public void drawEndScreen(){
        gc.setFont(new Font(canvas.getWidth()/10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        String message;
        if(this.coordinates.size() == (boundX/stepSize)*(boundY/stepSize)){
            message = "YOU WIN!!!!!";
        }else{
            message = "GAME OVER";
        }
        gc.fillText(message, canvas.getWidth()/2, canvas.getHeight()/2);
    }
    private void drawGrid(){
        //Draw Grid
        for (int i = 0; i < canvas.getHeight(); i += stepSize) {
            for (int j = 0; j < canvas.getWidth(); j += stepSize) {
                if ((i + j) / stepSize % 2 == 0) {
                    gc.setFill(Color.GREEN);
                } else {
                    gc.setFill(Color.LIGHTGREEN);
                }
                gc.fillRect(j, i, stepSize, stepSize);
            }
        }
    }
}
