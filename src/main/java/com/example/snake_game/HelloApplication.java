package com.example.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        build_menu(stage);
    }

    public static void main(String[] args) {
        launch();
    }
    private void build_menu(Stage stage){
        Group root = new Group();
        //Where the snake and food lives, a blank sheet of paper
        Canvas canvas = new Canvas(200, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //Draw Grid
        int gridSize = 20;
        for(int i = 0; i < canvas.getHeight(); i += gridSize){
            for(int j = 0; j < canvas.getWidth(); j += gridSize){
                if((i + j) / gridSize % 2 == 0){
                    gc.setFill(Color.GREEN);
                }else{
                    gc.setFill(Color.LIGHTGREEN);
                }
                gc.fillRect(j, i, gridSize, gridSize);
            }
        }
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, canvas.getWidth(), canvas.getHeight());
        stage.setTitle("Snake Game By Yulun");
        stage.setScene(scene);
        Snake snake = new Snake(23, gridSize, canvas);

        //Lambda is a good replacement for overwriting the entire handler.
        EventHandler<ActionEvent> event = e -> begin(snake, stage);
        Timeline game = new Timeline(new KeyFrame(Duration.millis(500), event));
        game.setCycleCount(Animation.INDEFINITE);
        game.play();
        //This event handler is more complicated, need to gather event data, so can't use lambda
        scene.setOnKeyPressed(keyEvent -> {
            switch(keyEvent.getCode()){
                case UP -> snake.changeDir(direction.UP);
                case DOWN -> snake.changeDir(direction.DOWN);
                case LEFT -> snake.changeDir(direction.LEFT);
                case RIGHT -> snake.changeDir(direction.RIGHT);
            }
        });
    }
    public void begin(Snake snake, Stage stage){
        if(snake.isAlive()){
            snake.move();
        }
        stage.show();

    }
}