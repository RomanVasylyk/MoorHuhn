package application;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.util.Random;
import javafx.scene.layout.VBox;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;

public class Main extends Application {
    private BorderPane root;
    private Random random;
    private HBox ammunitionBox;
    private MediaPlayer mediaPlayer;
    private AudioClip start;
    private ScoreCounter scoreCounter;
    private Timeline timer;
    private AudioClip timerSound;
    private AmmunitionManager ammunitionManager;
    private Label timerLabel;
    private int totalTimeInSeconds = 35;
    private AudioClip over;
    private BackgroundImage initialBackground;
    private int q = 0;
    private Scene scene;
    @Override
    public void start(Stage primaryStage) {
        try {
        	if(q==0) {
            start = new AudioClip(getClass().getResource("/game_start.mp3").toString());
            start.play();
            root = new BorderPane();
            Image initialBackgroundImage = new Image("fons.jpg");
            initialBackground = new BackgroundImage(
                    initialBackgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    null,
                    new BackgroundSize(
                            BackgroundSize.AUTO,
                            BackgroundSize.AUTO,
                            false,
                            false,
                            true,
                            true
                    )
            );

            root.setBackground(new Background(initialBackground));

            String musicFile = getClass().getResource("/ambientloop.mp3").toString();
            Media sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

            ammunitionBox = new HBox();
            ammunitionManager = new AmmunitionManager(ammunitionBox, 8);

            scoreCounter = new ScoreCounter();

            VBox topContainer = new VBox();
            topContainer.getChildren().addAll(ammunitionBox, scoreCounter);

            root.setTop(topContainer);
            timerLabel = new Label("Time: 35");
            timerLabel.setStyle("-fx-font-size: 36; -fx-text-fill: white;");
            VBox timerContainer = new VBox(timerLabel);
            timerContainer.setAlignment(Pos.CENTER);
            root.setBottom(timerContainer);

            timer = new Timeline(
                    
                    new KeyFrame(Duration.seconds(1), event -> {
                        updateTimerLabel();
                    })
                );
                timer.setCycleCount(Timeline.INDEFINITE);
                timer.play();

                Label reloadLabel = new Label("Reloading bullets on space press");
                reloadLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
                VBox reloadLabelContainer = new VBox(reloadLabel);
                reloadLabelContainer.setAlignment(Pos.CENTER);
                root.setCenter(reloadLabelContainer);

                Timeline hideLabel = new Timeline(
                        new KeyFrame(Duration.seconds(5), e -> {
                        	root.getChildren().remove(reloadLabelContainer);
                        })
                );
                hideLabel.setCycleCount(1);
                hideLabel.play();
                
            timerSound = new AudioClip(getClass().getResource("/time_running.mp3").toString());

            
            root.setOnMousePressed(event -> {
            	if (event.getTarget() == root) {
                    removeBullet();
                }
            });


           
            
            Scene scene = new Scene(root, 1500, 1000);
            this.scene=scene;
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            scene.setOnKeyPressed(event -> {
                if (event.getCode().toString().equals("SPACE")) {
                    reloadBullets();
                }
            });
            

            random = new Random();

            new AnimationTimer() {
                private long lastChickenTime = 0;

                @Override
                public void handle(long now) {
                    if (now - lastChickenTime >= 1_000_000_000) {
                        generateChicken();
                        lastChickenTime = now;
                    }
                }
            }.start();
            
            new AnimationTimer() {
                private long lastLeafTime = 0;

                @Override
                public void handle(long now) {
                    if (now - lastLeafTime >= 1_000_000_000) {
                        generateLeaf();
                        lastLeafTime = now;
                    }
                }
            }.start();
            Image cursorImage = new Image(getClass().getResource("/aim.png").toString());
            ImageView imageView = new ImageView(cursorImage);
            imageView.setFitWidth(30);  
            imageView.setFitHeight(30); 

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT); 
            scene.setCursor(new ImageCursor(imageView.snapshot(parameters, null)));

            primaryStage.setScene(scene);
            primaryStage.show();}
        	else {
        		System.out.println("nostart");
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateChicken() {
        if (totalTimeInSeconds > 0) {
            String size = getRandomSize();
            String direction = getRandomDirection();
            Chicken chicken = new Chicken(root, 1400, 1000, size, direction, ammunitionManager, scoreCounter);
            root.getChildren().add(chicken);
        }
    }

    private String getRandomSize() {
        String[] sizes = {"S", "M", "L"};
        return sizes[random.nextInt(sizes.length)];
    }

    private String getRandomDirection() {
        String[] directions = {"Left", "Right"};
        return directions[random.nextInt(directions.length)];
    }
    private void removeBullet() {
        ammunitionManager.removeBullet();
    }


    private void reloadBullets() {
        ammunitionManager.reloadBullets();
    }
    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.q=1;
        }
    }
    private void stopGame() {
        this.q = 1;
        timer.stop();
        timerSound.stop();
        mediaPlayer.stop();
        root.setBackground(null);
        root.getChildren().clear();
        over = new AudioClip(getClass().getResource("/game_over.mp3").toString());
        over.play();
        Image gameOverImage = new Image("over_bg.png");

        BackgroundImage background = new BackgroundImage(
                gameOverImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true
                )
        );

        root.setBackground(new Background(background));

        Label scoreLabel = new Label("Your Score: " + scoreCounter.getScore());
        scoreLabel.setStyle("-fx-font-size: 36; -fx-text-fill: white;");
        VBox scoreContainer = new VBox(scoreLabel);
        scoreContainer.setAlignment(Pos.CENTER);

        Button restartButton = new Button("Restart Game");
        restartButton.setStyle("-fx-font-size: 20;");
        restartButton.setOnAction(event -> {
            resetGame();
        });

        VBox buttonContainer = new VBox(restartButton);
        buttonContainer.setAlignment(Pos.CENTER);

        VBox centerContainer = new VBox(scoreContainer, buttonContainer);
        centerContainer.setAlignment(Pos.CENTER);

        root.setCenter(centerContainer);

        System.out.println("Game Over!");
    }


    private void resetGame() {
        totalTimeInSeconds = 30;
        scoreCounter.resetScore();
        ammunitionManager.resetAmmunition(); 
        root.getChildren().clear();
        this.q = 0;
        root.setBackground(null);
        root.getChildren().clear();
        
        startGame();
    }


    private void startGame() {
        root.setBackground(null);

        start.play();
        mediaPlayer.play();
        Image backgroundImage = new Image("fons.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true
                )
        );

        root.setBackground(new Background(background));

        Label reloadLabel = new Label("Reloading bullets on space press");
        reloadLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        VBox reloadLabelContainer = new VBox(reloadLabel);
        reloadLabelContainer.setAlignment(Pos.CENTER);
        root.setCenter(reloadLabelContainer);

        Timeline hideLabel = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                	root.getChildren().remove(reloadLabelContainer);
                })
        );
        hideLabel.setCycleCount(1);
        hideLabel.play();

        ammunitionBox.getChildren().clear();
        ammunitionManager = new AmmunitionManager(ammunitionBox, 8);
        scoreCounter = new ScoreCounter();

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    updateTimerLabel();
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        generateChicken();

        root.setTop(new VBox(ammunitionBox, scoreCounter));
        root.setBottom(new VBox(timerLabel));

        Image cursorImage = new Image(getClass().getResource("/aim.png").toString());
        ImageView imageView = new ImageView(cursorImage);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(imageView.snapshot(parameters, null)));
    }





    private void updateTimerLabel() {
        totalTimeInSeconds--; 
        Platform.runLater(() -> {
            timerLabel.setText("Time: " + totalTimeInSeconds);
            if (totalTimeInSeconds <= 0) {
                stopGame();
            } else if (totalTimeInSeconds <= 5) {
                
                    timerSound.play();
                
            }
        });
    }
    private void generateLeaf() {
        if (totalTimeInSeconds > 0) {
            Leaf leaf = new Leaf(root.getWidth() * Math.random(), 0, root, ammunitionManager, scoreCounter);

            if (!root.getChildren().contains(leaf)) {
                root.getChildren().add(leaf);
            }
        }
    }







    public static void main(String[] args) {
        launch(args);
    }
}
