package com.example.snake_game;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.control.Slider;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class Menu {
    private int menuHeight;
    private int menuWidth;
    private boolean pause;
    //speed of snake
    private int speed;
    //game grid size
    private int gridSize;
    private Canvas canvas;
    private GraphicsContext menuGc;
    private Slider slider;
    private ToggleGroup group;
    private RadioButton[] rbs;
    private String message;

    // default constructor should never be used
    Menu() {}
    Menu(Canvas c) {
        this.pause = true;
        // default values
        this.speed = 200;
        this.canvas = c;
        this.gridSize = 20; //easy would be 40, difficult would be 15
        this.menuHeight = (int)c.getHeight();
        this.menuWidth = (int)c.getWidth();
        menuGc = canvas.getGraphicsContext2D();
        menuGc.setTextAlign(TextAlignment.CENTER);
        menuGc.setTextBaseline(VPos.CENTER);
        menuGc.setFont(new Font(20));
        this.message = "Start";
        sliderInit();
        radioInit();
    }
    public void drawMenu(){
        // Speed selection
        menuGc.setFill(Color.BLACK);
        menuGc.fillRect(menuWidth/2,0, menuWidth/2,menuHeight/2);
        // Difficulty selection
        menuGc.setFill(Color.WHITE);
        menuGc.fillRect(menuWidth/2,menuHeight/2, menuWidth/2,menuHeight/2);
    }
    public void drawScore(int score){
        menuGc.setFill(Color.WHITE);
        menuGc.fillRect(0,0, menuWidth/2,menuHeight/2);
        menuGc.setFill(Color.BLACK);
        menuGc.fillText("Score: "+score, menuWidth/4, menuHeight/4);
    }
    public void drawMsg(){
        menuGc.setFill(Color.BLACK);
        menuGc.fillRect(0,menuHeight/2, menuWidth/2,menuHeight/2);
        menuGc.setFill(Color.WHITE);
        menuGc.fillText(message, menuWidth/4, 3*menuHeight/4);
    }
    public void togglePause(){
        this.pause = !this.pause;
    }
    public boolean isPause(){
        return this.pause;
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }
    public int getSpeed(){
        return this.speed;
    }
    public void setGridSize(int gridSize){
        this.gridSize = gridSize;
    }
    public int getGridSize(){
        return this.gridSize;
    }
    public Slider getSlider(){
        return this.slider;
    }
    public RadioButton[] getRBs(){
        return this.rbs;
    }
    public ToggleGroup getGroup(){
        return this.group;
    }

    private void sliderInit(){
        slider = new Slider(1, 3, 2);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);

        slider.setLayoutX(3*menuWidth/5);
        slider.setLayoutY(3*menuHeight/5);

        slider.setMajorTickUnit(0.25f);
        slider.setBlockIncrement(0.25f);
    }
    private void radioInit(){
        group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Easy");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        rb1.setLayoutX((menuWidth/2)+(menuWidth/4));

        // set this as default
        RadioButton rb2 = new RadioButton("Medium");
        rb2.setSelected(true);
        rb2.setToggleGroup(group);
        rb2.setLayoutX((menuWidth/2)+(menuWidth/2));

        RadioButton rb3 = new RadioButton("Hard");
        rb3.setToggleGroup(group);
        rb3.setLayoutX((menuWidth/2)+(3*menuWidth/4));
        RadioButton[] rb = {rb1, rb2, rb3};
        this.rbs = rb;
        for(int i = 0; i < rbs.length; i++){
            rbs[i].setAlignment(Pos.CENTER);
            rbs[i].setLayoutX((menuWidth/2)+((i+1)*menuWidth/8));
            rbs[i].setLayoutY(menuHeight/4);
            rbs[i].setStyle("-fx-text-fill: white;");
        }
    }
    public Canvas getCanvas(){
        return canvas;
    }
    public void setPause(boolean t){
        this.pause = t;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
