package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Leaf extends ImageView{
	private Image[] sprites;
	double maxWidth,maxHeight,rychlost;
	private Image[] spritesd;
    private int live = 19;
    private int dead = 22;
    private Pane root;
    private Timeline Timeline;
    int act = 0;
    int stav = 0;
    private AudioClip hit1;
    private String s="F";
    private AmmunitionManager ammunitionManager;
    private ScoreCounter scoreCounter;

	public Leaf(double x, int y, Pane root,AmmunitionManager ammunitionManager,ScoreCounter scoreCounter) {
		super();
		this.scoreCounter = scoreCounter;
 	   this.ammunitionManager = ammunitionManager;
		this.root=root;
		hit1 = new AudioClip(getClass().getResource("/leaf-hit.mp3").toString());
 	   sprites = new Image[live];
 	   for(int i = 0; i < live; i++) {
 	       sprites[i] = new Image("leaf"+i+".png");
 	   }
 	   spritesd = new Image[dead];
 	   for(int i = 0; i < dead; i++) {
 	       spritesd[i] = new Image("destroyed_leaf_"+i+".png");
 	   }
 	  setX(x);
      setY(y);

      Timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
          vykresli();
          move();
      }));
      Timeline.setCycleCount(Animation.INDEFINITE);
      Timeline.play();

      setOnMousePressed(evt -> onClick());

      root.getChildren().add(this);	
	
	}
	private void onClick() {
		if(this.stav==1 || this.stav==2) {
 		   System.out.println("Dead");
 	   }
 	   else if (ammunitionManager.removeBullet()) {
            stav = 1; 
     	   act=0;
     	  hit1.play();
     	   scoreCounter.increaseScore(this.s);
        }
 	   else {
 		   System.out.println("not bul");
 	   }
 	}
	private void move() {
		setY(getY() + 5); 
	    checkBounds();
    	
    }
	private void removeImage() {
        root.getChildren().remove(this);
        act = 0;
        stav = 0;
    }
	private void vykresli() {    
        if(this.stav==0) {
            setImage(sprites[act]);  
            act++;
            if(act >= sprites.length) act = 0;
        }
        if(this.stav==1) {
            setImage(spritesd[act]);
            act++;
            if(act >= spritesd.length) { 
                removeImage();
                act = 0;
            }
        }
    }
	private void checkBounds() {
	    if (getY() > root.getHeight()) {
	        removeImage();
	    }
	}
	
}
