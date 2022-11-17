package com.example.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import java.io.IOException;


public class SnakeGame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        build_menu(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void build_menu(Stage stage) {
        Group root = new Group();
        // menu size is fixed
        int menuWidth = 600;
        int menuHeight = 100;
        Canvas mCanvas = new Canvas(menuWidth, menuHeight);
        Menu menu = new Menu(mCanvas);
        //Where the snake and food lives, a blank sheet of paper
        Canvas canvas = new Canvas(600, 600);

        // move the game canvas down
        canvas.relocate(0, menuHeight);

        // Scene & stage config
        root.getChildren().add(canvas);
        root.getChildren().add(mCanvas);
        root.getChildren().add(menu.getSlider());
        root.getChildren().addAll(menu.getRBs());
        Scene scene = new Scene(root, canvas.getWidth(), canvas.getHeight() + mCanvas.getHeight());
        stage.setTitle("Snake Game By Yulun");
        stage.setScene(scene);

        // Initial menu display, player can customize and begin from here
        menu.drawMsg();
        menu.drawMenu();
        stage.show();
        scene.setUserData(true);
        scene.setOnMouseClicked(clickEvent -> {
            int mouseX = (int) clickEvent.getX();
            int mouseY = (int) clickEvent.getY();
            if (mouseY > menu.getCanvas().getHeight() / 2 && mouseX < menu.getCanvas().getWidth() / 2
            && mouseY < menu.getCanvas().getHeight() && mouseX > 0) {
                menu.togglePause();
                System.out.println("start game");
                play(stage, menu, canvas);
            }
        });

        // Event handler for speed selection
        menu.getSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                System.out.println(menu.getSlider().getValue());
                menu.setSpeed((int)(400 - 200 * menu.getSlider().getValue() / 2));
            }

        });

        // Event handler for grid size selection
        menu.getGroup().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n) {
                RadioButton rb = (RadioButton) menu.getGroup().getSelectedToggle();
                if (rb != null) {
                    switch(rb.getText()){
                        case "Easy" -> menu.setGridSize(40);
                        case "Medium" -> menu.setGridSize(20);
                        case "Hard" -> menu.setGridSize(15);
                    }
                }
                canvas.requestFocus();
            }
        });
    }

    // Actual content of the game
    public void play(Stage stage, Menu menu, Canvas canvas){
        //Initialize a snake using data configured in the menu
        final Snake snake = new Snake(2, menu.getGridSize(), canvas);

        //Lambda is a good replacement for overwriting the entire handler.
        //game runs at menu.getSpeed() frame rate, each frame processes the event specified
        Timeline game = new Timeline(new KeyFrame(Duration.millis(menu.getSpeed()),
                e -> begin(snake, menu, stage)));

        // Want the animation to continue until interrupted
        game.setCycleCount(Animation.INDEFINITE);
        game.play();

        // Gets called when the game is stopped, since the game is asynchronous
        game.statusProperty().addListener(new ChangeListener<Animation.Status>() {
            @Override
            public void changed(ObservableValue<? extends Animation.Status> observableValue,
                                Animation.Status status, Animation.Status t1) {
                if (t1 == Animation.Status.STOPPED) {
                    System.out.println("restarting");
                    // Restart the game, menu configuration is preserved
                    play(stage, menu, canvas);
                }
            }
        });

        // Change snake direction, or pause
        stage.getScene().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP -> snake.changeDir(direction.UP);
                case DOWN -> snake.changeDir(direction.DOWN);
                case LEFT -> snake.changeDir(direction.LEFT);
                case RIGHT -> snake.changeDir(direction.RIGHT);
                case SPACE -> menu.togglePause();
            }
            // ensures the canvas is getting arrow key, not slider or radio button
            canvas.requestFocus();
            keyEvent.consume();
        });

        // Menu navigation for the start/pause/resume/restart button
        stage.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent clickEvent) {
                int mouseX = (int) clickEvent.getX();
                int mouseY = (int) clickEvent.getY();
                if (mouseX < menu.getCanvas().getWidth() && mouseX > 0
                        && mouseY < menu.getCanvas().getHeight() && mouseY > 0) {
                    if (mouseX < menu.getCanvas().getWidth() / 2) {
                        if (mouseY > menu.getCanvas().getHeight() / 2) {
                            if(menu.getMessage().equals("Restart")){
                                game.stop();
                                menu.setMessage("Play");
                            }else{
                                menu.togglePause();
                                if(menu.isPause()){
                                    menu.setMessage("Resume");
                                }else{
                                    menu.setMessage("Pause");
                                }
                            }
                            menu.drawMsg();
                        }
                    }
                }
                canvas.requestFocus();
                clickEvent.consume();
            }
        });
    }

    // game process this every frame, updates image for every frame
    // and detects death.
    public void begin(Snake snake, Menu menu, Stage stage) {
        if (!menu.isPause() && snake.isAlive()) {
            snake.move();
        }
        if (!snake.isAlive()) {
            menu.setPause(true);
            menu.setMessage("Restart");
            menu.drawMsg();
            snake.drawEndScreen();
        }
        menu.drawScore(snake.getLength()-2);
        menu.drawMenu();
        stage.show();
    }
}