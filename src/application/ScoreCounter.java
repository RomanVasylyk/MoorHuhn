package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ScoreCounter extends HBox {
    private int score;
    private Label scoreLabel;
    

    public ScoreCounter() {
        this.score = 0;
        this.scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 24;"); 
        getChildren().add(scoreLabel);
        setAlignment(Pos.TOP_RIGHT); 
        setPadding(new Insets(10));
    }

    public void increaseScore(String s) {
    	if(s=="L")score+=10;
    	if(s=="M")score+=15;
    	if(s=="S")score+=20;
    	if(s=="F")score+=25;
        scoreLabel.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }
    public void resetScore() {
        score = 0;
    }
}
